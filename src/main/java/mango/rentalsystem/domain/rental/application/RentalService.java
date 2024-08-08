package mango.rentalsystem.domain.rental.application;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.dao.RentalRepository;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.domain.rental.dto.request.RentalRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.response.RentalCreateResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService {

	private final MemberRepository memberRepository;
	private final RentalRepository rentalRepository;
	private final ItemRepository itemRepository;

	public RentalCreateResponse createRental(String studentId, RentalRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		DailyRentalTime todayRentalTime = member.getDepartment().getTodayRentalTime();

		if (LocalTime.now().isBefore(todayRentalTime.getRentalStartTime()) &&
			LocalTime.now().isAfter(todayRentalTime.getRentalEndTime())) {
			throw new RuntimeException("대여 가능 시간이 아닙니다.");
		} // 검증 편의 메서드 Department에 추가 예정

		Item item = itemRepository.findById(request.itemId())
			.orElseThrow(() -> new RuntimeException("존재하지 않는 물품입니다."));

		// itemStatus가 IDLE인지 확인하는 메서드

		Rental rental = Rental.createInitialRental(member, item);
		Rental savedRental = rentalRepository.save(rental);

		return RentalCreateResponse.from(savedRental);
	}

	public void updateRentalStatus(Long rentalId, RentalStatusUpdateRequest request) {
		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 Id 입니다."));

		RentalStatus currentStatus = rental.getRentalStatus();
		RentalStatus requestStatus = request.rentalStatus();

		switch (currentStatus) {
			case APPROVAL_REQUESTED -> {
				switch (requestStatus) {
					case CANCELED -> rental.updateRentalStatusToCanceled();
					case APPROVED -> rental.updateRentalStatusToApproved();
					case REJECTED -> rental.updateRentalStatusToRejected();
					default -> throw new IllegalStateException(currentStatus.name() + requestStatus.name());
				}
			}
			case APPROVED -> {
				switch (requestStatus) {
					case BORROW -> rental.updateRentalStatusToBorrow();
					default -> throw new IllegalStateException(currentStatus.name() + requestStatus.name());
				}
			}
			case BORROW, OVERDUE -> {
				switch (requestStatus) {
					case RETURN_REQUESTED -> rental.updateRentalStatusToReturnRequested();
					default -> throw new IllegalStateException(currentStatus.name() + requestStatus.name());
				}
			}
			case RETURN_REQUESTED -> {
				switch (requestStatus) {
					case RETURN -> rental.updateRentalStatusToReturn();
					default -> throw new IllegalStateException(currentStatus.name() + requestStatus.name());
				}
			}
			default -> throw new IllegalStateException(currentStatus.name() + requestStatus.name());
		}
	}

	public void updateRentalStatusToOverdue() {

	}
}
