package mango.rentalsystem.domain.rental.application;

import static mango.rentalsystem.domain.member.domain.MemberRole.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.department.domain.DailyRentalTime;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.dao.RentalRepository;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.domain.rental.dto.request.RentalCreateRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalReviewRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.response.RentalCreateResponse;
import mango.rentalsystem.domain.rental.dto.response.RentalFindResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService {

	private final MemberRepository memberRepository;
	private final RentalRepository rentalRepository;
	private final ItemRepository itemRepository;

	public RentalCreateResponse createRental(String studentId, RentalCreateRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		// Member의 rentalBannedDate 검증 필요

		DailyRentalTime todayRentalTime = member.getDepartment().getTodayRentalTime();

		if (LocalTime.now().isBefore(todayRentalTime.getRentalStartTime()) ||
			LocalTime.now().isAfter(todayRentalTime.getRentalEndTime())) {
			throw new RuntimeException("대여 가능 시간이 아닙니다.");
		} // 검증 편의 메서드 Department에 추가 예정

		Item item = itemRepository.findById(request.itemId())
			.orElseThrow(() -> new RuntimeException("존재하지 않는 물품입니다."));

		if (!(member.getDepartment().equals(item.getCategory().getDepartment()))) {
			throw new RuntimeException("소속 학과 물품이 아닙니다.");
		}

		// itemStatus가 IDLE인지 검증하는 메서드
		// itemStatus를 BOOK으로 변경하는 메서드

		Rental rental = Rental.createInitialRental(member, item);
		Rental savedRental = rentalRepository.save(rental);

		return RentalCreateResponse.from(savedRental);
	}

	public List<RentalFindResponse> findAllRental(String studentId) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		Department department = member.getDepartment();

		List<Rental> allRentalList = rentalRepository.findAllByMemberDepartment(department);

		List<RentalFindResponse> response = new ArrayList<>();
		for (Rental rental : allRentalList) {
			response.add(RentalFindResponse.from(rental));
		}
		return response;
	}

	public List<RentalFindResponse> findMyRental(String studentId) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		List<Rental> myRentalList = rentalRepository.findAllByMember(member);

		List<RentalFindResponse> response = new ArrayList<>();
		for (Rental rental : myRentalList) {
			response.add(RentalFindResponse.from(rental));
		}
		return response;
	}

	public RentalFindResponse findRental(String studentId, Long rentalId) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 Id 입니다."));

		validateAuthForRental(member, rental);

		return RentalFindResponse.from(rental);
	}

	public void updateRentalStatus(String studentId, Long rentalId, RentalStatusUpdateRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		DailyRentalTime todayRentalTime = member.getDepartment().getTodayRentalTime();

		if (LocalTime.now().isBefore(todayRentalTime.getRentalStartTime()) ||
			LocalTime.now().isAfter(todayRentalTime.getRentalEndTime())) {
			throw new RuntimeException("대여 가능 시간이 아닙니다.");
		} // 검증 편의 메서드 Department에 추가 예정

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 Id 입니다."));

		validateAuthForRental(member, rental);

		RentalStatus currentStatus = rental.getRentalStatus();
		RentalStatus requestStatus = request.rentalStatus();

		switch (currentStatus) {
			case APPROVAL_REQUESTED -> {
				switch (requestStatus) {
					case CANCELED -> rental.updateRentalStatusToCanceled(); // CANCELED를 admin만 할 수 있게 해야하나 고민중
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

	// 단일 스레드에서 실행. 다른 scheduling 메서드 생기면 @Async로 비동기 처리 필요.
	@Scheduled(fixedRate = 10000) // 10초 주기로 실행
	public void updateRentalStatusToOverdue() {
		LocalDateTime now = LocalDateTime.now();
		List<Rental> overdueRentalList = rentalRepository
			.findAllByRentalStatusAndDeadlineDateTimeBefore(RentalStatus.BORROW, now);

		for (Rental rental : overdueRentalList) {
			rental.updateRentalStatusToOverdue();
		}
	}

	public void updateRentalReview(String studentId, Long rentalId, RentalReviewRequest request) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대여 Id 입니다."));

		validateAuthForRental(member, rental);

		if (rental.getRentalStatus() == RentalStatus.RETURN) {
			rental.updateRentalReview(request.rentalReview());
		} else {
			throw new IllegalStateException(rental.getRentalStatus().name());
		}
	}

	private void validateAuthForRental(Member member, Rental rental) {
		Member rentalMember = rental.getMember();
		if (member.getRole() == ROLE_MEMBER && !(rentalMember.equals(member))
			|| member.getRole() == ROLE_ADMIN && !(rentalMember.getDepartment().equals(member.getDepartment()))) {
			throw new IllegalStateException("권한이 없습니다.");
		}
	}
}
