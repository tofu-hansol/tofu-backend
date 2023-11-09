package com.hansol.tofu.upload.image;

import static com.hansol.tofu.error.ErrorCode.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hansol.tofu.error.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ObjectStorageServiceImpl implements StorageService {

    private final AmazonS3 amazonS3;
    private final S3Component component;
	public static final String CDN_URL = "http://qefsedhmghte20150888.cdn.ntruss.com/";
	public static final String OPTIMIZE_RULE = "?type=m&w=1024&h=1024&quality=80";

    @Override
    public String uploadImage(MultipartFile file, String folderName) {
        String filename = createFilename(file.getOriginalFilename());
        String pathWithName = folderName + filename;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try {
            InputStream inputStream = file.getInputStream();
            uploadFile(inputStream, objectMetadata, pathWithName);
        } catch (IOException e) {
            throw new BaseException(FAILED_TO_UPLOAD);
        }
        return getFileUrl(pathWithName);
    }

    @Override
    public void getBuckets() {
        try {
            List<Bucket> buckets = amazonS3.listBuckets();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files, String folderName) {

        return files.stream()
                .map(file -> uploadImage(file, folderName))
                .collect(Collectors.toList());

    }

    @Override
    public Boolean deleteImage(String key) {
       try {
           DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(component.getBucketName(), key);
           amazonS3.deleteObject(deleteObjectRequest);

           return true;
       } catch (Exception e) {
           throw new BaseException(FAILED_TO_UPLOAD);
       }
    }

    private String createFilename(String originalFilename) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    private String getFileExtension(String filename) {
        try {
            return filename.substring(filename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new BaseException(INVALID_FILETYPE);
        }

    }

    private void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String pathWithName) {
       amazonS3.putObject(new PutObjectRequest(component.getBucketName(), pathWithName, inputStream, objectMetadata)
               .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String getFileUrl(String pathWithName) {
       return CDN_URL + pathWithName + OPTIMIZE_RULE;
    }
}
