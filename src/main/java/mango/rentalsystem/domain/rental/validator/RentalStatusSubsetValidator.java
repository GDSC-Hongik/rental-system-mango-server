package mango.rentalsystem.domain.rental.validator;

import static mango.rentalsystem.domain.member.domain.MemberRole.*;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mango.rentalsystem.domain.member.domain.MemberRole;
import mango.rentalsystem.domain.rental.domain.RentalStatus;
import mango.rentalsystem.global.security.AuthDetails;

public class RentalStatusSubsetValidator implements ConstraintValidator<RentalStatusSubset, RentalStatus> {
	RentalStatus[] memberSubset;
	RentalStatus[] adminSubset;

	@Override
	public void initialize(RentalStatusSubset constraintAnnotation) {
		this.memberSubset = constraintAnnotation.memberAvailableStatus();
		this.adminSubset = constraintAnnotation.adminAvailableStatus();
	}

	@Override
	public boolean isValid(RentalStatus rentalStatus, ConstraintValidatorContext context) {
		MemberRole memberRole = currentMemberRole();
		if ((memberRole == ROLE_MEMBER)
			&& !(memberSubset == null || Arrays.asList(memberSubset).contains(rentalStatus))
			|| (memberRole == ROLE_ADMIN)
			&& !(adminSubset == null || Arrays.asList(adminSubset).contains(rentalStatus))) {
			setRoleErrorMessage(context, memberRole);
			return false;
		}
		return true;
	}

	private MemberRole currentMemberRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AuthDetails authDetails = (AuthDetails)authentication.getPrincipal();
		return valueOf(authDetails.getMemberRole());
	}

	private void setRoleErrorMessage(ConstraintValidatorContext context, MemberRole memberRole) {
		RentalStatus[] subset = memberRole == ROLE_MEMBER ? memberSubset : adminSubset;
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(memberRole.name() + "은 " + Arrays.toString(
			subset) + " 중 하나의 상태로만 요청이 가능합니다.").addConstraintViolation();
	}
}
