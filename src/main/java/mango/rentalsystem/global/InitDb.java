package mango.rentalsystem.global;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.category.domain.Category;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.member.domain.Member;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.computerDepartmentInit();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final EntityManager em;
		private final BCryptPasswordEncoder passwordEncoder;

		public void computerDepartmentInit() {
			// departmentInit
			Map<DayOfWeek, DailyRentalTime> weeklyRentalTime = new HashMap<>();

			DailyRentalTime weekdayRentalTime = DailyRentalTime.createDailyRentalTime(
				LocalTime.of(9, 0),
				LocalTime.of(16, 30),
				//department에 LocalTime 예외로직 필요. deadline은 rentalend보다 뒤여야함.
				1,
				LocalTime.of(17, 0)
			);
			for (DayOfWeek day : DayOfWeek.values()) {
				weeklyRentalTime.put(day, weekdayRentalTime);
			}

			DailyRentalTime fridayRentalTime = DailyRentalTime.createDailyRentalTime(
				LocalTime.of(9, 0),
				LocalTime.of(16, 30),
				0,
				LocalTime.of(17, 0)
			);
			weeklyRentalTime.put(DayOfWeek.FRIDAY, fridayRentalTime);

			DailyRentalTime weekendRentalTime = DailyRentalTime.createDailyRentalTime(
				LocalTime.of(0, 0),
				LocalTime.of(0, 0),
				0,
				LocalTime.of(0, 0)
			);
			weeklyRentalTime.put(DayOfWeek.SATURDAY, weekendRentalTime);
			weeklyRentalTime.put(DayOfWeek.SUNDAY, weekendRentalTime);

			Department departmentComputer = Department.createDepartment("컴퓨터공학과", weeklyRentalTime);
			em.persist(departmentComputer);

			// memberInit
			Member computerAdmin = Member.createAdmin("ComputerAdmin",
				passwordEncoder.encode("password"),
				"컴공관리자",
				departmentComputer,
				"010-1234-5678");
			em.persist(computerAdmin);

			// categoryInit
			Category categoryComputerA = Category.create("A", departmentComputer, "컴공A");
			em.persist(categoryComputerA);
			Category categoryComputerB = Category.create("B", departmentComputer, "컴공B");
			em.persist(categoryComputerB);

			// itemInit
			Item itemA = Item.create(categoryComputerA);
			Item itemB = Item.create(categoryComputerA);
			Item itemC = Item.create(categoryComputerB);
			Item itemD = Item.create(categoryComputerB);
			em.persist(itemA);
			em.persist(itemB);
			em.persist(itemC);
			em.persist(itemD);

			// rentalInit
		}
	}
}
