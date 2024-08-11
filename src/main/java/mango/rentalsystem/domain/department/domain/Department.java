package mango.rentalsystem.domain.department.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {

	@Id
	@GeneratedValue
	@Column(name = "department_id")
	private Long id;

	private String name;

	private String rentalPlace;

	private String notice;

	@ElementCollection
	@CollectionTable(name = "weekly_rental_time", joinColumns = @JoinColumn(name = "department_id"))
	@MapKeyColumn(name = "day_of_week")
	@MapKeyEnumerated(EnumType.STRING)
	private Map<DayOfWeek, DailyRentalTime> weeklyRentalTime = new HashMap<>();

	@Builder(access = AccessLevel.PRIVATE)
	private Department(String name, String rentalPlace, Map<DayOfWeek, DailyRentalTime> weeklyRentalTime,
		String notice) {
		this.name = name;
		this.rentalPlace = rentalPlace;
		this.weeklyRentalTime = weeklyRentalTime;
		this.notice = notice;
	}

	public static Department createDepartment(String name, Map<DayOfWeek, DailyRentalTime> weeklyRentalTime) {
		return Department.builder().name(name).weeklyRentalTime(weeklyRentalTime).build();
	}

	public void updateDepartmentInfo(String name, String rentalPlace, String notice) {
		this.name = name;
		this.rentalPlace = rentalPlace;
		this.notice = notice;
	}

	public void updateWeeklyRentalTime(Map<DayOfWeek, DailyRentalTime> weeklyRentalTime) {
		this.weeklyRentalTime = weeklyRentalTime;
	}

	public DailyRentalTime getTodayRentalTime() {
		return this.weeklyRentalTime.get(LocalDate.now().getDayOfWeek());
	}
}
