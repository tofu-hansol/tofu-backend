package com.hansol.tofu.category;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hansol.tofu.category.domain.CategoryResponseDTO;
import com.hansol.tofu.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = SQLException.class)
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CategoryResponseDTO> getCategoryList() {
		return categoryRepository.findAll().stream().map(CategoryResponseDTO::toDTO).toList();
	}

}
