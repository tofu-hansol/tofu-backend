package com.hansol.tofu.upload.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	String uploadImage(MultipartFile file, String folderName);

	List<String> uploadImages(List<MultipartFile> files, String folderName);

	Boolean deleteImage(String key);

	void getBuckets();
}