package mango.rentalsystem.domain.rental.dto.request;

import jakarta.validation.constraints.NotNull;

public record RentalCreateRequest(@NotNull(message = "아이템 id를 입력해주세요") Long itemId) {
}
