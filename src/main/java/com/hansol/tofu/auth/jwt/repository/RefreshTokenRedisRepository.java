package com.hansol.tofu.auth.jwt.repository;

import org.springframework.data.repository.CrudRepository;

import com.hansol.tofu.auth.jwt.dto.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
