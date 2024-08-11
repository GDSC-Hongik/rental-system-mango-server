package mango.rentalsystem.domain.category.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDetailResponse {
	private Long categoryId;
	private String name;
	private	String description;
	private Long departmentId;

	public CategoryDetailResponse(Long categoryId, String name, String description, Long departmentId) {
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
		this.departmentId = departmentId;
	}
}
