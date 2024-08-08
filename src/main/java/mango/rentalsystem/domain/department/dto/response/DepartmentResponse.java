package mango.rentalsystem.domain.department.dto.response;

import java.time.DayOfWeek;
import java.util.Map;

import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.department.domain.Department;

public record DepartmentResponse(
	String name,
	String rentalPlace,
	String notice,
	Map<DayOfWeek, DailyRentalTime> weeklyRentalTime) {

	public static DepartmentResponse from(Department department) {
		return new DepartmentResponse(
			department.getName(),
			department.getRentalPlace(),
			department.getNotice(),
			department.getWeeklyRentalTime()
		);
	}
}
