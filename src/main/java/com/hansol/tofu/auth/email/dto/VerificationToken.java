package com.hansol.tofu.auth.email.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@RedisHash(value = "email", timeToLive = 60 * 30)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class VerificationToken {

    @Id
    private String email;

    @Indexed
    private String code;

    @TimeToLive
    private Long expiration;
}
