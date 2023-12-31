package com.hansol.tofu.auth.filter.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import static com.hansol.tofu.auth.filter.jwt.JwtTokenProvider.REFRESH_TOKEN_TIMEOUT;

@Getter
@Builder
@RedisHash(value = "refresh", timeToLive = REFRESH_TOKEN_TIMEOUT)
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
