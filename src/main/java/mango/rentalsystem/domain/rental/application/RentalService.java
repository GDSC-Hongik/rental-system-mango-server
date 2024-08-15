package mango.rentalsystem.domain.rental.application;

import static mango.rentalsystem.domain.member.domain.MemberRole.*;
import static mango.rentalsystem.global.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.item.dao.ItemRepository;
import mango.rentalsystem.domain.item.domain.Item;
import mango.rentalsystem.domain.item.domain.ItemStatus;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.dao.RentalRepository;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.domain.rental.dto.request.RentalCreateRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalFindRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalReviewRequest;
import mango.rentalsystem.domain.rental.dto.request.RentalStatusUpdateRequest;
import mango.rentalsystem.domain.rental.dto.response.RentalCreateResponse;
import mango.rentalsystem.domain.rental.dto.response.RentalFindResponse;
import mango.rentalsystem.global.exception.CustomException;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService {

	private final MemberRepository memberRepository;
	private final RentalRepository rentalRepository;
	private final ItemRepository itemRepository;

	public RentalCreateResponse createRental(String loginId, RentalCreateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		member.validateRentalBannedDate();

		member.getDepartment().validateRentalTime();

		Item item = itemRepository.findById(request.itemId())
			.orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

		// item 예외 처리 로직
		if (!(member.getDepartment().equals(item.getCategory().getDepartment()))) {
			throw new CustomException(UNAUTHORIZED_ITEM);
		}
		if (item.getItemStatus() != ItemStatus.IDLE) {
			throw new CustomException(INVALID_ITEM_STATUS);
		}
		item.updateItemStatus(ItemStatus.BOOK);

		Rental rental = Rental.createInitialRental(member, item);
		Rental savedRental = rentalRepository.save(rental);

		return RentalCreateResponse.from(savedRental);
	}

	public List<RentalFindResponse> findAllRental(String loginId, RentalFindRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Department department = member.getDepartment();

		List<Rental> allRentalList;
		if (request == null) {
			allRentalList = rentalRepository.findAllByMemberDepartment(department);
		} else {
			List<RentalStatus> rentalStatuses = request.rentalStatuses();
			allRentalList = rentalRepository.findAllByRentalStatusInAndMemberDepartment(rentalStatuses, department);
		}

		return allRentalList.stream()
			.map(RentalFindResponse::from)
			.collect(Collectors.toList());
	}

	public List<RentalFindResponse> findMyRental(String loginId, RentalFindRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		List<Rental> myRentalList;
		if (request == null) {
			myRentalList = rentalRepository.findAllByMember(member);
		} else {
			List<RentalStatus> rentalStatuses = request.rentalStatuses();
			myRentalList = rentalRepository.findAllByRentalStatusInAndMember(rentalStatuses, member);
		}

		return myRentalList.stream()
			.map(RentalFindResponse::from)
			.collect(Collectors.toList());
	}

	public RentalFindResponse findRental(String loginId, Long rentalId) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new CustomException(RENTAL_NOT_FOUND));

		validateAuthForRental(member, rental);

		return RentalFindResponse.from(rental);
	}

	public void updateRentalStatus(String loginId, Long rentalId, RentalStatusUpdateRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		member.getDepartment().validateRentalTime();

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new CustomException(RENTAL_NOT_FOUND));

		validateAuthForRental(member, rental);

		RentalStatus currentStatus = rental.getRentalStatus();
		RentalStatus requestStatus = request.rentalStatus();

		switch (currentStatus) {
			case APPROVAL_REQUESTED -> {
				switch (requestStatus) {
					case CANCELED -> rental.updateRentalStatusToCanceled(); // CANCELED를 admin만 할 수 있게 해야하나 고민중
					case APPROVED -> rental.updateRentalStatusToApproved();
					case REJECTED -> rental.updateRentalStatusToRejected();
					default -> throw new CustomException(INVALID_RENTAL_STATUS);
				}
			}
			case APPROVED -> {
				switch (requestStatus) {
					case BORROW -> rental.updateRentalStatusToBorrow();
					default -> throw new CustomException(INVALID_RENTAL_STATUS);
				}
			}
			case BORROW, OVERDUE -> {
				switch (requestStatus) {
					case RETURN_REQUESTED -> rental.updateRentalStatusToReturnRequested();
					default -> throw new CustomException(INVALID_RENTAL_STATUS);
				}
			}
			case RETURN_REQUESTED -> {
				switch (requestStatus) {
					case RETURN -> rental.updateRentalStatusToReturn();
					default -> throw new CustomException(INVALID_RENTAL_STATUS);
				}
			}
			default -> throw new CustomException(INVALID_RENTAL_STATUS);
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

	public void updateRentalReview(String loginId, Long rentalId, RentalReviewRequest request) {
		Member member = memberRepository.findByStudentId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		Rental rental = rentalRepository.findById(rentalId)
			.orElseThrow(() -> new CustomException(RENTAL_NOT_FOUND));

		validateAuthForRental(member, rental);

		if (rental.getRentalStatus() == RentalStatus.RETURN) {
			rental.updateRentalReview(request.rentalReview());
		} else {
			throw new CustomException(INVALID_RENTAL_REVIEW);
		}
	}

	private void validateAuthForRental(Member member, Rental rental) {
		Member rentalMember = rental.getMember();
		if (member.getRole() == ROLE_MEMBER && !(rentalMember.equals(member))
			|| member.getRole() == ROLE_ADMIN && !(rentalMember.getDepartment().equals(member.getDepartment()))) {
			throw new CustomException(UNAUTHORIZED_RENTAL);
		}
	}
}
