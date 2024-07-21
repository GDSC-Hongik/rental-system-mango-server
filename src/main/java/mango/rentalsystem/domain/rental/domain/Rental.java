package mango.rentalsystem.domain.rental.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

	private int rentalReview;
}
