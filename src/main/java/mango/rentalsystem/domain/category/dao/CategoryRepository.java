package mango.rentalsystem.domain.category.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.department.domain.Department;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByNameAndDepartment(String categoryName, Department department);

	Optional<Category> findById(Long categoryId);
}
