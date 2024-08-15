package mango.rentalsystem.domain.category.api;

import java.util.List;

import javax.xml.transform.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import mango.rentalsystem.domain.auth.domain.LoginUser;
import mango.rentalsystem.domain.category.application.CategoryService;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.category.dto.request.CategoryCreateRequest;
import mango.rentalsystem.domain.category.dto.request.CategoryModifyRequest;
import mango.rentalsystem.domain.category.dto.response.CategoryDetailResponse;
import mango.rentalsystem.domain.category.dto.response.CategorySummaryResponse;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	// 카테고리 전체 조회
	@PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
	@GetMapping
	public ResponseEntity<List<CategorySummaryResponse>> getAllCategories(@LoginUser String loginId) {
		return ResponseEntity.ok(categoryService.findAll(loginId));
	}

	// 카테고리 추가
	@PreAuthorize("hasRole('ADMIN')")    // ADMIN 검사
	@PostMapping
	public ResponseEntity<Void> createCategory(@LoginUser String loginId,
		@Valid @RequestBody CategoryCreateRequest request) {
		categoryService.createCategory(loginId, request);
		return ResponseEntity.ok().build();
	}

	// 특정 카테고리 정보 조회
	@PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDetailResponse> getCategoryDetail(@LoginUser String loginId,
		@PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.getCategoryById(loginId, categoryId));
	}

	// 특정 카테고리 정보 변경
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{categoryId}")
	public ResponseEntity<CategorySummaryResponse> modifyCategory(@LoginUser String loginId,
		@Valid @RequestBody CategoryModifyRequest request, @PathVariable Long categoryId) {
		return ResponseEntity.ok(categoryService.modifyCategory(loginId, request, categoryId));
	}

	// 특정 카테고리 삭제
	@PreAuthorize("hasRole('ADMIN')")    // ADMIN 검사
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<Void> deleteCategory(@LoginUser String loginId, @PathVariable Long categoryId) {
		categoryService.deleteCategory(loginId, categoryId);
		return ResponseEntity.ok().build();
	}
}
