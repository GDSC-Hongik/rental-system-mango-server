package mango.rentalsystem.domain.item.application;

import static mango.rentalsystem.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.dao.CategoryRepository;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.dto.request.ItemCreateRequest;
import mango.rentalsystem.domain.item.dto.request.ItemModifyRequest;
import mango.rentalsystem.domain.item.dto.response.ItemDetailResponse;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.global.exception.CustomException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void createItem(String loginId, ItemCreateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		// Category 객체 조회
		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(()->new CustomException(CATEGORY_NOT_FOUND));

		validateAuthForCategory(member, category);

		Item item = Item.create(category);
		itemRepository.save(item);
	}

	/**
	 * 특정 물품 조회
	 */
	public ItemDetailResponse getItemById(String loginId, Long itemId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Item item = itemRepository.findById(itemId)
			.orElseThrow(()->new CustomException(ITEM_NOT_FOUND));

		validateAuthForCategory(member, item.getCategory());

		return ItemDetailResponse.from(item);
	}

	/**
	 * 특정 물품 정보 변경
	 */
	@Transactional
	public ItemDetailResponse modifyItem (String loginId, ItemModifyRequest request, Long itemId){
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Item item = itemRepository.findById(itemId)
			.orElseThrow(()->new CustomException(ITEM_NOT_FOUND));

		validateAuthForCategory(member, item.getCategory());

		item.updateItemStatus(request.itemStatus());
		itemRepository.save(item);

		return ItemDetailResponse.from(item);
	}

	/**
	 * 특정 카테고리 삭제
	 */
	@Transactional
	public void deleteItem(String loginId, Long itemId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Item item = itemRepository.findById(itemId)
			.orElseThrow(()->new CustomException(ITEM_NOT_FOUND));

		validateAuthForCategory(member, item.getCategory());

		itemRepository.deleteById(itemId);
	}

	private void validateAuthForCategory(Member member, Category category) {
		Department memberDepartment = member.getDepartment();
		Department categoryDepartment = category.getDepartment();
		if (!(memberDepartment.equals(categoryDepartment))) {
			throw new CustomException(UNAUTHORIZED_CATEGORY);
		}
	}
}