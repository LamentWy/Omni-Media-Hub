package com.lament.z.omni.mhub.scan;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.io.MoreFiles;
import com.lament.z.omni.mhub.model.ResourceCollection;
import com.lament.z.omni.mhub.model.ResourceRelation;
import com.lament.z.omni.mhub.model.Video;
import com.lament.z.omni.mhub.model.vm.ResRootVM;
import com.lament.z.omni.mhub.repository.ResourceCollectionRepository;
import com.lament.z.omni.mhub.repository.ResourceRelationRepository;
import com.lament.z.omni.mhub.repository.VideoRepository;
import com.lament.z.omni.mhub.security.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 资源扫描器
 * <p>
 * 文件扫描直接上 guava，偷懒神器。不扫描快捷方式,符号链接之类的“文件”，避免死循环。
 * 文件类型仅根据文件后缀进行判断和分类。
 * */
@Service
public class OmniResourceScanner {

	private static final Logger log = LoggerFactory.getLogger(OmniResourceScanner.class);
	private final VideoRepository videoRepository;
	private final ResourceCollectionRepository resourceCollectionRepository;
	private final ResourceRelationRepository resourceRelationRepository;


	@Value("${omni-media-hub.scanner.root-dir}")
	private String rootDir;

	public OmniResourceScanner(VideoRepository videoRepository, ResourceCollectionRepository resourceCollectionRepository, ResourceRelationRepository resourceRelationRepository) {
		this.videoRepository = videoRepository;
		this.resourceCollectionRepository = resourceCollectionRepository;
		this.resourceRelationRepository = resourceRelationRepository;
	}

	@SuppressWarnings("UnstableApiUsage")
	public Mono<Iterable<Path>> scan(ResRootVM resRootVM) {

		if (resRootVM != null && resRootVM.getRootPath() != null && !resRootVM.getRootPath().isEmpty()){
			rootDir = resRootVM.getRootPath();
		}

		if (rootDir == null || rootDir.isEmpty()) {
			log.error("rootDir is null or empty");
			return Mono.error(new IllegalArgumentException( rootDir + " is null or empty"));
		}

		Path rootPath = Paths.get(rootDir);
		if (!Files.isDirectory(rootPath)){
			log.error("{} is not a directory", rootPath);
			return Mono.error(new IllegalArgumentException( rootPath + " is not a directory"));
		}

		return Mono.just(MoreFiles.fileTraverser().depthFirstPostOrder(Paths.get(rootDir)));
	}

	/**
	 * 对 fileTraverser 返回的文件集合进行处理。
	 * <p>
	 *  这部分代码最初是非响应式风格编写的。
	 *  修改之后看起来似乎是“不太对劲”，尤其是 switch case 的部分。
	 *  */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Mono<ScanResult> process(Iterable<Path> paths) {
		ScanResult scanResult = new ScanResult();

		return Flux.fromIterable(paths)
				.filter(path -> !Files.isDirectory(path))
				.doOnEach(path -> log.info("Processing path {}", path))
				.publishOn(Schedulers.boundedElastic())
				.flatMap(path -> processByResourceType(path, scanResult))
				 .collectList()
				 .flatMap(scanResults -> {
					 Optional<ScanResult> total = scanResults.stream()
							 .reduce((scanResult1, scanResult2) -> {
								 ScanResult sum = new ScanResult();
								 int c = scanResult1.getCollectionCount()
										 .getAndAdd(scanResult2.getCollectionCount().intValue());
								 int v = scanResult1.getVideoCount()
										 .getAndAdd(scanResult2.getVideoCount().intValue());
								 sum.setCollectionCount(new AtomicInteger(c));
								 sum.setVideoCount(new AtomicInteger(v));
								 return sum;
							 });
					 return Mono.justOrEmpty(total);
				 });


	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Mono<ScanResult> processByResourceType(Path path,ScanResult scanResult) {

		File file = path.toFile();
		String cName = file.getParentFile().getName();
		String rName = file.getName();
		String filePath = file.getAbsolutePath();
		String fileExtension = MoreFiles.getFileExtension(path);
		ResourceType rType = FileType.determineResourceType(fileExtension);
		switch (rType) {
			case VIDEO -> {
				return processVideoResource(cName, scanResult, rName, filePath);
			}
			case AUDIO -> log.debug("音频分支，暂未实现");
			case BOOK -> log.debug("书籍分支，暂未实现");
			case PIC -> log.debug("图集 or 漫画分支，暂未实现");
			case UNKNOWN -> log.warn("不支持的文件类型: {}", fileExtension);
			default -> log.error("unknown rType error: [{}] | ignore and keep scan", rType);
		}
		return Mono.just(scanResult);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Mono<ScanResult> processVideoResource(String cName, ScanResult scanResult, String rName, String filePath) {
		Mono<ResourceCollection> rcMono = getOrCreateRCIfNotExist(cName, scanResult);

		Video video = new Video(rName, filePath);
		Mono<Video> videoMono = videoRepository.save(video)
				.switchIfEmpty(Mono.error(new ResourceScanException("Save Video Resource Exception")))
				.doOnSuccess(savedVideo -> {
					scanResult.getVideoCount().getAndIncrement();
					log.info("save [{}] into video", rName);
				});


		ResourceRelation rrFromZip = Mono.zip(rcMono, videoMono, (savedResCollection, savedVideo) -> {
			ResourceRelation rr = new ResourceRelation();
			rr.setCollectionId(savedResCollection.getId());
			rr.setVideoId(savedVideo.getId());
			return rr;
		}).block();

		return resourceRelationRepository.save(rrFromZip)
				.switchIfEmpty(Mono.error(new ResourceScanException("Save RR exception")))
				.doOnSuccess(savedRR -> log.info("save [{}]-[{}] into resource relation", savedRR.getCollectionId(), savedRR.getVideoId()))
				.then(Mono.just(scanResult));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public Mono<ResourceCollection> getOrCreateRCIfNotExist(String cName, ScanResult scanResult) {
		return resourceCollectionRepository.findOneByName(cName)
				.switchIfEmpty(Mono.just(new ResourceCollection()))
				.flatMap(existRC -> {
					if (existRC.getId() == null){
						ResourceCollection newRC = new ResourceCollection();
						newRC.setName(cName);
						newRC.setDescription("扫描自动生成。");
						newRC.setVisible(0);
						newRC.setCreatedBy(SecurityConstants.SYSTEM);
						newRC.setLastModifiedBy(SecurityConstants.SYSTEM);
						return resourceCollectionRepository.save(newRC)
								.switchIfEmpty(Mono.error(new ResourceScanException("Save RC exception")))
								.doOnSuccess(savedRc -> {
									scanResult.getCollectionCount().getAndIncrement();
									log.info("Create Resource Collection: [{}]", cName);
								});
					}else {
						return Mono.just(existRC);
					}
				});


	}

}
