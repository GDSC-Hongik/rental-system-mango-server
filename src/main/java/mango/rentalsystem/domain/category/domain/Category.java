package mango.rentalsystem.domain.category.domain;

import jakarta.persistence.*;
import lombok.Getter;

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
}
