package mango.rentalsystem.domain.category.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.application.CategoryService;
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
}
