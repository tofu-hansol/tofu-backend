package com.hansol.tofu.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hansol.tofu.category.domain.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
