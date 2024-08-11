package mango.rentalsystem.domain.department.domain;

import java.time.Duration;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
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

	private Duration rentalDeadLine;

	public DailyRentalTime(LocalTime rentalStartTime, LocalTime rentalEndTime, Duration rentalDeadLine) {

	}

	// 팩토리 메서드: DailyRentalTime 객체를 생성하고 반환합니다.
	public static DailyRentalTime createDailyRentalTime(LocalTime rentalStartTime, LocalTime rentalEndTime, int deadlineMinutesAfterEnd, LocalTime actualDeadline) {
		Duration rentalDeadLine = Duration.ofMinutes(deadlineMinutesAfterEnd);

		// 실제 종료 시간과 입력된 종료 시간을 비교하여 예외 처리
		if (actualDeadline.isBefore(rentalEndTime)) {
			throw new IllegalArgumentException("Actual deadline must be after or equal to the rental end time.");
		}

		return new DailyRentalTime(rentalStartTime, rentalEndTime, rentalDeadLine);
	}
}
