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
import mango.rentalsystem.domain.item.domain.ItemStatus;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.member.domain.MemberRole;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.departmentMemberInit();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final EntityManager em;
		private final BCryptPasswordEncoder passwordEncoder;

		public void departmentMemberInit() {
			// departmentInit
			Map<DayOfWeek, DailyRentalTime> weeklyRentalTimeComputer = new HashMap<>();
			Map<DayOfWeek, DailyRentalTime> weeklyRentalTimeData = new HashMap<>();

			for (DayOfWeek day : DayOfWeek.values()) {
				DailyRentalTime dailyRentalTimeComputer = DailyRentalTime.createDailyRentalTime(
					LocalTime.of(10, 0),
					LocalTime.of(17, 55),
					0,
					LocalTime.of(17, 55)
				);
				DailyRentalTime dailyRentalTimeData = DailyRentalTime.createDailyRentalTime(
					LocalTime.of(10, 0),
					LocalTime.of(17, 55),
					0,
					LocalTime.of(17, 55)
				);
				weeklyRentalTimeComputer.put(day, dailyRentalTimeComputer);
				weeklyRentalTimeData.put(day, dailyRentalTimeData);
			}

			Department departmentComputer = Department.createDepartment("컴퓨터공학과", weeklyRentalTimeComputer);
			em.persist(departmentComputer);
			Department departmentData = Department.createDepartment("산업데이터공학과", weeklyRentalTimeData);
			em.persist(departmentData);

			// memberInit
			Member computerAdmin = Member.builder()
				.studentId("ComputerAdmin")
				.password(passwordEncoder.encode("password"))
				.name("컴공관리자")
				.role(MemberRole.ROLE_ADMIN)
				.department(departmentComputer)
				.build();
			em.persist(computerAdmin);
			Member member1 = Member.builder()
				.studentId("A123456")
				.password(passwordEncoder.encode("password"))
				.name("컴공멤버1")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentComputer)
				.build();
			em.persist(member1);
			Member member2 = Member.builder()
				.studentId("B123456")
				.password(passwordEncoder.encode("password"))
				.name("컴공멤버2")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentComputer)
				.build();
			em.persist(member2);
			Member member3 = Member.builder()
				.studentId("C123456")
				.password(passwordEncoder.encode("password"))
				.name("컴공멤버3")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentComputer)
				.build();
			em.persist(member3);

			Member dataAdmin = Member.builder()
				.studentId("DataAdmin")
				.password(passwordEncoder.encode("password"))
				.name("산데관리자")
				.role(MemberRole.ROLE_ADMIN)
				.department(departmentData)
				.build();
			em.persist(dataAdmin);
			Member member4 = Member.builder()
				.studentId("D123456")
				.password(passwordEncoder.encode("password"))
				.name("산데멤버1")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentData)
				.build();
			em.persist(member4);
			Member member5 = Member.builder()
				.studentId("E123456")
				.password(passwordEncoder.encode("password"))
				.name("산데멤버2")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentData)
				.build();
			em.persist(member5);
			Member member6 = Member.builder()
				.studentId("F123456")
				.password(passwordEncoder.encode("password"))
				.name("산데멤버3")
				.role(MemberRole.ROLE_MEMBER)
				.department(departmentData)
				.build();
			em.persist(member6);

			// categoryInit
			Category categoryComputerA = Category.create("A", departmentComputer, "컴공A");
			em.persist(categoryComputerA);
			Category categoryComputerB = Category.create("B", departmentComputer, "컴공B");
			em.persist(categoryComputerB);
			Category categoryDataC = Category.create("C", departmentData, "산데C");
			em.persist(categoryDataC);
			Category categoryDataD = Category.create("D", departmentData, "산데D");
			em.persist(categoryDataD);

			// itemInit
			Item itemA = Item.builder()
				.category(categoryComputerA)
				.itemStatus(ItemStatus.IDLE)
				.itemReview((double)0)
				.build();
			em.persist(itemA);

			Item itemB = Item.builder()
				.category(categoryComputerB)
				.itemStatus(ItemStatus.IDLE)
				.itemReview((double)0)
				.build();
			em.persist(itemB);

			Item itemC = Item.builder()
				.category(categoryDataC)
				.itemStatus(ItemStatus.IDLE)
				.itemReview((double)0)
				.build();
			em.persist(itemC);

			Item itemD = Item.builder()
				.category(categoryDataD)
				.itemStatus(ItemStatus.IDLE)
				.itemReview((double)0)
				.build();
			em.persist(itemD);

			Item itemE = Item.builder()
				.category(categoryComputerA)
				.itemStatus(ItemStatus.IDLE)
				.itemReview((double)0)
				.build();
			em.persist(itemE);

			// rentalInit
		}
	}
}