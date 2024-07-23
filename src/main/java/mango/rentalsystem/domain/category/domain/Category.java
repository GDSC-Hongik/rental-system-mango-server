package mango.rentalsystem.domain.category.domain;

import jakarta.persistence.*;
import lombok.Getter;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {

	@Id
	@GeneratedValue
	@Column(name = "category_id")
	private Long id;

	private String name;

	private String description;
	// description: 22년형 macbook air 14인치

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToMany(mappedBy = "category")
	private List<Item> items = new ArrayList<>();
}
