package mango.rentalsystem.domain.department.domain;

import java.time.Duration;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRentalTime {

	private LocalTime rentalStartTime;

	private LocalTime rentalEndTime;

	private int rentalDeadLineDate; // today.plusDays(rentalDeadLineDate) 처럼 이용.

	private LocalTime rentalDeadLineTime;

	@Builder(access = AccessLevel.PRIVATE)
	private DailyRentalTime(LocalTime rentalStartTime, LocalTime rentalEndTime, int rentalDeadLineDate, LocalTime rentalDeadLineTime) {
		this.rentalStartTime = rentalStartTime;
		this.rentalEndTime = rentalEndTime;
		this.rentalDeadLineDate = rentalDeadLineDate;
		this.rentalDeadLineTime = rentalDeadLineTime;
	}

	public static DailyRentalTime createDailyRentalTime(LocalTime rentalStartTime, LocalTime rentalEndTime,
		int rentalDeadLineDate, LocalTime rentalDeadLineTime) {
		return DailyRentalTime.builder()
			.rentalStartTime(rentalStartTime)
			.rentalEndTime(rentalEndTime)
			.rentalDeadLineDate(rentalDeadLineDate)
			.rentalDeadLineTime(rentalDeadLineTime)
			.build();
	}
}
