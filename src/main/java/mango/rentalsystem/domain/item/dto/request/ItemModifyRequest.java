package mango.rentalsystem.domain.item.dto.request;

import jakarta.validation.constraints.NotNull;
import mango.rentalsystem.domain.item.domain.ItemStatus;

public record ItemModifyRequest(
	@NotNull(message = "물품 상태를 입력해주세요.")
	ItemStatus itemStatus) {
}
