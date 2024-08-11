package mango.rentalsystem.domain.category.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

	@Id
	@GeneratedValue
	@Column(name = "category_id")
	@NotNull
	private Long id;

	@NotNull
	@Column(unique = true)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	@NotNull
	private Department department;

	private String description;
	// description: 22년형 macbook air 14인치

	@OneToMany(mappedBy = "category")
	private List<Item> items = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	private	Category(
		String name,
		String description,
		Department department
		) {
		this.name = name;
		this.description = description;
		this.department = department;
	}

	public static Category create(String name, Department department, String description) {
		return Category.builder()
			.name(name)
			.department(department)
			.description(description)
			.build();
	}
}
