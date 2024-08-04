package mango.rentalsystem.domain.category.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import mango.rentalsystem.domain.category.application.CategoryService;
import mango.rentalsystem.domain.category.dto.request.CategoryCreateRequest;
import mango.rentalsystem.domain.category.dto.response.CategoryResponse;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	// 카테고리 전체 조회
	@GetMapping
	public ResponseEntity<List<CategoryResponse>> getAllCategories() {
		List<CategoryResponse> response = categoryService.findAll();
		return ResponseEntity.ok().body(response);
	}

	// 카테고리 추가
	@PostMapping
	public ResponseEntity<Void> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
		categoryService.createCategory(request);
		return ResponseEntity.ok().build();
	}

}
