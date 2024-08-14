package mango.rentalsystem.domain.item.application;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.dao.CategoryRepository;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.dto.request.ItemCreateRequest;
import mango.rentalsystem.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;

	@Transactional
	public void createItem(ItemCreateRequest request) {
		// Category 객체 조회
		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(()->new CustomException(CATEGORY_NOT_FOUND));

		Item item = Item.create(category, request.itemStatus(), request.itemReview());
		itemRepository.save(item);
	}
}