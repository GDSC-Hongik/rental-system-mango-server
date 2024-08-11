package mango.rentalsystem.domain.category.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mango.rentalsystem.domain.category.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
