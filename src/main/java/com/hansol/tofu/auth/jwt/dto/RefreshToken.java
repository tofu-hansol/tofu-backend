package com.hansol.tofu.auth.jwt.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@RedisHash("refresh")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	private String email;

	@Indexed
	private String refreshToken;

	@TimeToLive
	private Long expiration;

	private RefreshToken(String email, String refreshToken, Long expiration) {
		this.email = email;
		this.refreshToken = refreshToken;
		this.expiration = expiration;
	}
}
