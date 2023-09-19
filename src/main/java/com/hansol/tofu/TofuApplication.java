package com.hansol.tofu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableConfigurationProperties
@EnableJpaAuditing
@ConfigurationPropertiesScan(basePackages = {"com.hansol.tofu.config", "com.hansol.tofu.auth", "com.hansol.tofu.upload"})
@SpringBootApplication
public class TofuApplication {

	public static void main(String[] args) {
		SpringApplication.run(TofuApplication.class, args);
	}

}
