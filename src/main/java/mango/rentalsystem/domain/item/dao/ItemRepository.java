package mango.rentalsystem.domain.item.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mango.rentalsystem.domain.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	// 카테고리 ID를 기반으로 Item 목록을 조회하는 메소드
	List<Item> findByCategoryId(Long categoryId);
}