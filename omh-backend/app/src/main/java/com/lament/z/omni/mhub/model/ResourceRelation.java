package com.lament.z.omni.mhub.model;

import java.util.StringJoiner;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


/**
 * 资源和集合的关联关系。
 * */
@Table
public class ResourceRelation {

	@Id
	private Long id;

	@Column("c_id")
	private Long collectionId;

	@Column("v_id")
	private Long videoId;

	@Column("a_id")
	private Long audioId;

	@Column("b_id")
	private Long bookId;

	@Column("p_id")
	private Long pictureId;

	public ResourceRelation() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long getAudioId() {
		return audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ResourceRelation.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("collectionId=" + collectionId)
				.add("videoId=" + videoId)
				.add("audioId=" + audioId)
				.add("bookId=" + bookId)
				.add("pictureId=" + pictureId)
				.toString();
	}
}
