package com.hansol.tofu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
		// Problem: transform Error : Handler dispatch failed: java.lang.NoSuchMethodError: 'java.lang.Object org.hibernate.ScrollableResults.get(int)
		// solve(Reference): https://velog.io/@dktlsk6/QueryDSL-transform-%EC%97%90%EB%9F%AC
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }
}
