package mango.rentalsystem.domain.rental.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rental {

	@Id
	@GeneratedValue
	@Column(name = "rental_id")
	private Long id; //pk

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id") //fk
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id") //fk
	private Item item;

	@Enumerated(EnumType.STRING)
	private RentalStatus rentalStatus;

	private LocalDateTime borrowDateTime;

	private LocalDateTime returnDateTime;

	private LocalDateTime deadlineDateTime;

	private int rentalReview;

	@Builder(access = AccessLevel.PRIVATE)
	private Rental(Member member, Item item, RentalStatus rentalStatus, LocalDateTime borrowDateTime,
		LocalDateTime returnDateTime, LocalDateTime deadlineDateTime, int rentalReview) {
		this.member = member;
		this.item = item;
		this.rentalStatus = rentalStatus;
		this.borrowDateTime = borrowDateTime;
		this.returnDateTime = returnDateTime;
		this.deadlineDateTime = deadlineDateTime;
		this.rentalReview = rentalReview;
	}

	public static Rental createInitialRental(Member member, Item item) {
		return Rental.builder()
			.member(member)
			.item(item)
			.rentalStatus(RentalStatus.APPROVAL_REQUESTED)
			.borrowDateTime(LocalDateTime.now())
			.deadlineDateTime(calculateDeadlineDateTime(member))
			.build();
	}

	private static LocalDateTime calculateDeadlineDateTime(Member member) {
		DailyRentalTime todayRentalTime = member.getDepartment()
			.getWeeklyRentalTime()
			.get(LocalDate.now().getDayOfWeek()); // DailyRentalTime 클래스에 메서드 만들기.
		return LocalDateTime.of(LocalDate.now().plusDays(todayRentalTime.getRentalDeadLineDate()),
			todayRentalTime.getRentalDeadLineTime());
	}

	public void updateRentalStatusToReturn() {
		this.rentalStatus = RentalStatus.RETURN;
		this.returnDateTime = LocalDateTime.now();
	}
}
