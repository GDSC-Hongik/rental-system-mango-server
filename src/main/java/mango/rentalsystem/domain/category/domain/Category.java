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

	@ManyToOne
	@JoinColumn(name = "department")
	private Department department;

	@OneToMany(mappedBy = "Item")
	private List<Item> items = new ArrayList<>();
}
