package mango.rentalsystem.domain.department.dto.request;

import java.time.DayOfWeek;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;

public record DepartmentRentalTimeUpdateRequest(
	@NotNull(message = "대여업무 시간 정보를 입력해주세요.")
	Map<DayOfWeek, DailyRentalTime> weeklyRentalTime) {
}
