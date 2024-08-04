package mango.rentalsystem.domain.category.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mango.rentalsystem.domain.category.dao.CategoryRepository;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.category.dto.request.CategoryCreateRequest;
import mango.rentalsystem.domain.category.dto.response.CategoryResponse;
import mango.rentalsystem.domain.department.dao.DepartmentRepository;
import mango.rentalsystem.domain.department.domain.Department;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final DepartmentRepository departmentRepository;

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
	@Transactional
	public void createCategory(CategoryCreateRequest request) {
		// Department 객체 조회
		Department department = departmentRepository.findById(request.departmentId())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 학과 ID입니다."));

		// Description이 null인 경우 빈 문자열로 설정
		String description = request.description() != null ? request.description() : "";

		// Category 객체 생성
		Category category = Category.create(request.name(), department, request.description());

		// ADMIN인지 검사
		// 해당 학과에 이미 있는 카테고리인지 중복 검사
		// 학과/물품종류명 모두 일치하는 경우 -> 예외 처리
		// 학과 or 물품종류명이 하나라도 다르면 성공

		// Category 저장
		categoryRepository.save(category);

		// 로그 출력
		log.info("[CategoryService] 카테고리 추가: categoryId={}", category.getId());
	}

	/**
	 * 카테고리 삭제
	 */
	// ADMIN인지 검사

	// category 최소 하나 있는지 확인
	// 카테고리 0개만 삭제 불가
	// 에러 띄우기 -> exception

}
