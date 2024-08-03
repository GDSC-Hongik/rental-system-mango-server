package mango.rentalsystem.domain.category.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.dao.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	/**
	 * 카테고리 조회
	 */
	// MEMBER인지 검사

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
