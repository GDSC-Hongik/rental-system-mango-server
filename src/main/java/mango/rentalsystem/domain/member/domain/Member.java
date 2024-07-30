package mango.rentalsystem.domain.member.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.rental.domain.Rental;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	private String studentId;

	private String password;

	private String name;


	@Enumerated(EnumType.STRING)
	@Builder.Default
	private MemberRole role = MemberRole.MEMBER;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	private String phone;

	private String pictureUrl;

	private boolean absenceStatus;

	@OneToMany(mappedBy = "member")
	@Builder.Default
	private List<Rental> rentalList = new ArrayList<>();

	private LocalDate rentalBannedDate;
}
