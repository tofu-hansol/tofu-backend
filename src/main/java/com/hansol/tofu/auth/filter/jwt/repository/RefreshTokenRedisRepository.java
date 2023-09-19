package com.hansol.tofu.auth.filter.jwt.repository;

import org.springframework.data.repository.CrudRepository;

import com.hansol.tofu.auth.filter.jwt.dto.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
