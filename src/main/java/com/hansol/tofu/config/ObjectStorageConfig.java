package com.hansol.tofu.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.hansol.tofu.upload.image.S3Component;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObjectStorageConfig {

    private final S3Component s3Component;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Component.getEndPoint(), s3Component.getRegionName()))
            .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3Component.getAccessKey(), s3Component.getSecretKey())))
            .build();
    }
}
