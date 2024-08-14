package mango.rentalsystem.domain.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.item.application.ItemService;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.dto.request.ItemCreateRequest;
import mango.rentalsystem.domain.item.dto.request.ItemModifyRequest;
import mango.rentalsystem.domain.item.dto.response.ItemDetailResponse;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService itemService;

	// 아이템 추가
	@PreAuthorize("hasRole('ADMIN')")	// ADMIN 검사
	@PostMapping
	public ResponseEntity<Void> createItem(@Valid @RequestBody ItemCreateRequest request) {
		itemService.createItem(request);
		return ResponseEntity.ok().build();
	}

	// 특정 물품 정보 조회
	@PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
	@GetMapping("/{itemId}")
	public ItemDetailResponse getItemDetail(@PathVariable Long itemId){
		Item item = itemService.getItemById(itemId);
		return ItemDetailResponse.from(item);
	}

	// 특정 물품 정보 변경
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{itemId}")
	public ResponseEntity<ItemDetailResponse> modifyItem(@Valid @RequestBody ItemModifyRequest request, @PathVariable Long itemId) {
		itemService.modifyItem(request, itemId);
		ItemDetailResponse response = itemService.modifyItem(request, itemId);
		return ResponseEntity.ok(response);
	}

	// 특정 물품 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{itemId}")
	public ResponseEntity<Void>deleteItem(@PathVariable Long itemId) {
		itemService.deleteItem(itemId);
		return ResponseEntity.ok().build();
	}
}
