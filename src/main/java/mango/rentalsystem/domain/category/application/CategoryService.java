package mango.rentalsystem.domain.category.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.dao.CategoryRepository;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.category.dto.response.CategoryResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
	private final CategoryRepository categoryRepository;

	/**
	 * 카테고리 전체 조회
	 */
	public List<CategoryResponse> findAll() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map(CategoryResponse::of).collect(Collectors.toList());
	}


	/**
	 * 카테고리 추가
	 */
	// ADMIN인지 검사

	// 이미 있는 카테고리인지 중복 검사

	/**
	 * 카테고리 삭제
	 */
	// ADMIN인지 검사

	// category 최소 하나 있는지 확인
	// 카테고리 0개만 삭제 불가
	// 에러 띄우기 -> exception

}
