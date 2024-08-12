package mango.rentalsystem.domain.category.application;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mango.rentalsystem.domain.category.dao.CategoryRepository;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.category.dto.request.CategoryCreateRequest;
import mango.rentalsystem.domain.category.dto.request.CategoryModifyRequest;
import mango.rentalsystem.domain.category.dto.response.CategorySummaryResponse;
import mango.rentalsystem.domain.department.dao.DepartmentRepository;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.global.exception.CustomException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final DepartmentRepository departmentRepository;
	private final ItemRepository itemRepository;


	/**
	 * 카테고리 전체 조회
	 */
	public List<CategorySummaryResponse> findAll() {
		return categoryRepository.findAll().stream()
			.map(o -> new CategorySummaryResponse(o.getId(), o.getName(), o.getDescription()))
			.collect(Collectors.toList());
	}

	/**
	 * 카테고리 추가
	 */
	@Transactional
	public void createCategory(CategoryCreateRequest request) {
		// Department 객체 조회
		Department department = departmentRepository.findById(request.departmentId())
			.orElseThrow(()->new CustomException(INVALID_DEPARTMENT_ID));

		// Description이 null인 경우 빈 문자열로 설정
		String description = request.description() != null ? request.description() : "";

		// 중복 검사
		validateNoDuplicates(request.name(), department);

		// 중복 검사 통과하면 Category 객체 생성
		Category category = Category.create(request.name(), department, description);

		// Category 저장
		categoryRepository.save(category);

		// 로그 출력
		log.info("[CategoryService] 카테고리 추가: categoryId={}", category.getId());
	}

	/**
	 * 특정 카테고리 조회
	*/
	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));
	}

	/**
	 * 특정 카테고리 정보 변경
	 */
	@Transactional
	public CategorySummaryResponse modifyCategory (CategoryModifyRequest request, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(()-> new CustomException(CATEGORY_NOT_FOUND));

		String description = request.description() != null ? request.description() : "";
		category.modify(request.name(), description);
		categoryRepository.save(category);

		return CategorySummaryResponse.of(category);
	}

	/**
	 * 특정 카테고리 삭제
	 */
	@Transactional
	public void deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
			.orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));
		// 카테고리와 관련된 아이템을 먼저 삭제
		itemRepository.deleteByCategoryId(categoryId);

		// 그 후에 카테고리 삭제
		categoryRepository.deleteById(categoryId);
	}


	private void validateNoDuplicates(String categoryName, Department department) {
		Optional<Category> existingCategory = categoryRepository.findByNameAndDepartment(categoryName, department);

		if (existingCategory.isPresent()) {	// 기존에 이미 존재하는 카테고리라면
			throw new CustomException(DUPLICATE_CATEGORY);
		}
	}

}
