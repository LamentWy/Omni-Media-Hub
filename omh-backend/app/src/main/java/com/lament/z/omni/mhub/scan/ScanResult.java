package com.lament.z.omni.mhub.scan;

import java.util.concurrent.atomic.AtomicInteger;

public class ScanResult {
	private AtomicInteger collectionCount = new AtomicInteger(0);
	private AtomicInteger videoCount = new AtomicInteger(0);
	private AtomicInteger audioCount = new AtomicInteger(0);
	private AtomicInteger bookCount = new AtomicInteger(0);
	private AtomicInteger picturesCount = new AtomicInteger(0);

	public ScanResult() {
		//
	}

	public AtomicInteger getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(AtomicInteger collectionCount) {
		this.collectionCount = collectionCount;
	}

	public AtomicInteger getVideoCount() {
		return videoCount;
	}

	public void setVideoCount(AtomicInteger videoCount) {
		this.videoCount = videoCount;
	}

	public AtomicInteger getAudioCount() {
		return audioCount;
	}

	public void setAudioCount(AtomicInteger audioCount) {
		this.audioCount = audioCount;
	}

	public AtomicInteger getBookCount() {
		return bookCount;
	}

	public void setBookCount(AtomicInteger bookCount) {
		this.bookCount = bookCount;
	}

	public AtomicInteger getPicturesCount() {
		return picturesCount;
	}

	public void setPicturesCount(AtomicInteger picturesCount) {
		this.picturesCount = picturesCount;
	}
}
