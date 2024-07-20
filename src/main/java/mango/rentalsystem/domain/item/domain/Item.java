package mango.rentalsystem.domain.item.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import mango.rentalsystem.domain.category.domain.Category;

@Entity
@Getter
public class Item {
	@Id
	@GeneratedValue
	@Column(name = "item_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "category")
	private Category category;

	private ItemStatus itemStatus;

	private Double itemReview;
}
