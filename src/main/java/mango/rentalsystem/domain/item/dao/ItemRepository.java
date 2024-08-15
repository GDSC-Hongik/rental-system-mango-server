package mango.rentalsystem.domain.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import mango.rentalsystem.domain.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}