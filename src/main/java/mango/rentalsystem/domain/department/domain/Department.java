package mango.rentalsystem.domain.department.domain;

import java.time.DayOfWeek;
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
import lombok.Getter;

@Entity
@Getter
public class Department {

	@Id
	@GeneratedValue
	@Column(name = "department_id")
	private Long id;

	private String name;

	@ElementCollection
	@CollectionTable(name = "weekly_rental_time", joinColumns = @JoinColumn(name = "department_id"))
	@MapKeyColumn(name = "day_of_week")
	@MapKeyEnumerated(EnumType.STRING)
	private Map<DayOfWeek, DailyRentalTime> weeklyRentalTime = new HashMap<>();

	private String place;

	private String notice;
}
