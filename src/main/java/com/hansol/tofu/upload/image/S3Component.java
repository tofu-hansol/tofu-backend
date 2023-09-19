package com.hansol.tofu.upload.image;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties(prefix = "ncloud.storage")
public class S3Component {

	@NotBlank
 	private final String endPoint;

	@NotBlank
	private final String regionName;

	@NotBlank
	private final String accessKey;

	@NotBlank
	private final String secretKey;

	@NotBlank
	private final String bucketName;

	@ConstructorBinding
	public S3Component(String endPoint, String regionName, String accessKey, String secretKey, String bucketName) {
		this.endPoint = endPoint;
		this.regionName = regionName;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.bucketName = bucketName;
	}
}