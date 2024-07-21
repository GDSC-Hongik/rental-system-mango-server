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
		this.rentalStartTime = rentalStartTime;
		this.rentalEndTime = rentalEndTime;
		this.rentalDeadLine = rentalDeadLine;
	}
}
