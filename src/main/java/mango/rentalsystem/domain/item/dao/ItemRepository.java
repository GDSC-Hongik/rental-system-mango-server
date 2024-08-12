package mango.rentalsystem.domain.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import io.lettuce.core.dynamic.annotation.Param;
import mango.rentalsystem.domain.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	@Modifying
	@Query("DELETE FROM Item i WHERE i.category.id = :categoryId")
	void deleteByCategoryId(@Param("categoryId") Long categoryId);
}