package mango.rentalsystem.domain.member.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.global.exception.CustomException;
import mango.rentalsystem.global.exception.ErrorCode;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String studentId;

	private String password;

	private String name;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	private String phone;

	private String pictureUrl;

	private boolean absenceStatus;

	private LocalDate rentalBannedDate;

	@Builder
	private Member(String studentId, String password, String name, MemberRole role, Department department, String phone,
		String pictureUrl, boolean absenceStatus, LocalDate rentalBannedDate) {
		this.studentId = studentId;
		this.password = password;
		this.name = name;
		this.role = role;
		this.department = department;
		this.phone = phone;
		this.pictureUrl = pictureUrl;
		this.absenceStatus = absenceStatus;
		this.rentalBannedDate = rentalBannedDate;
	}

	public void validateRentalBannedDate() {
		if (!(LocalDate.now().isAfter(rentalBannedDate))) {
			throw new CustomException(ErrorCode.RENTAL_BANNED);
		}
	}

	public void updateRentalBannedDate() {
		this.rentalBannedDate = LocalDate.now().plusDays(3);
	}
}
