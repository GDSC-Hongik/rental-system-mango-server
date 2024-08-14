package mango.rentalsystem.domain.rental.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.domain.ItemStatus;
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

	private LocalDateTime requestDateTime;

	private LocalDateTime borrowDateTime;

	private LocalDateTime returnDateTime;

	private LocalDateTime deadlineDateTime;

	private Integer rentalReview;

	@Builder(access = AccessLevel.PRIVATE)
	private Rental(Member member, Item item, RentalStatus rentalStatus, LocalDateTime requestDateTime,
		LocalDateTime borrowDateTime, LocalDateTime returnDateTime, LocalDateTime deadlineDateTime,
		Integer rentalReview) {
		this.member = member;
		this.item = item;
		this.rentalStatus = rentalStatus;
		this.requestDateTime = requestDateTime;
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
			.requestDateTime(LocalDateTime.now())
			.deadlineDateTime(calculateDeadlineDateTime(member))
			.build();
	}

	private static LocalDateTime calculateDeadlineDateTime(Member member) {
		DailyRentalTime todayRentalTime = member.getDepartment().getTodayRentalTime();
		return LocalDateTime.of(LocalDate.now().plusDays(todayRentalTime.getRentalDeadLineDate()),
			todayRentalTime.getRentalDeadLineTime());
	}

	public void updateRentalStatusToCanceled() {
		this.rentalStatus = RentalStatus.CANCELED;
		this.item.updateItemStatus(ItemStatus.IDLE);
	}

	public void updateRentalStatusToApproved() {
		this.rentalStatus = RentalStatus.APPROVED;
		this.item.updateItemStatus(ItemStatus.BOOK);
	}

	public void updateRentalStatusToRejected() {
		this.rentalStatus = RentalStatus.REJECTED;
		this.item.updateItemStatus(ItemStatus.IDLE);
	}

	public void updateRentalStatusToBorrow() {
		this.rentalStatus = RentalStatus.BORROW;
		this.borrowDateTime = LocalDateTime.now();
		this.item.updateItemStatus(ItemStatus.BORROW);
	}

	public void updateRentalStatusToOverdue() {
		this.rentalStatus = RentalStatus.OVERDUE;
		this.item.updateItemStatus(ItemStatus.OVERDUE);
		// member rentalBannedDate 설정 로직
		// BannedDate 언제까지인지, 연장 필요한지 논의 필요
	}

	public void updateRentalStatusToReturnRequested() {
		this.rentalStatus = RentalStatus.RETURN_REQUESTED;
	}

	public void updateRentalStatusToReturn() {
		this.rentalStatus = RentalStatus.RETURN;
		this.returnDateTime = LocalDateTime.now();
		this.item.updateItemStatus(ItemStatus.IDLE);
	}

	public void updateRentalReview(Integer rentalReview) {
		this.rentalReview = rentalReview;
		this.item.updateItemReview(rentalReview);
	}
}
