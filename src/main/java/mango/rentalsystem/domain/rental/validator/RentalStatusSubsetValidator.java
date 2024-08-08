package mango.rentalsystem.domain.rental.validator;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

public class RentalStatusSubsetValidator implements ConstraintValidator<RentalStatusSubset, RentalStatus> {
	private RentalStatus[] subset;

	@Override
	public void initialize(RentalStatusSubset constraintAnnotation) {
		this.subset = constraintAnnotation.anyOf();
	}

	@Override
	public boolean isValid(RentalStatus rentalStatus, ConstraintValidatorContext constraintValidatorContext) {
		if (subset == null) {
			return true;
		}
		return Arrays.asList(subset).contains(rentalStatus);
	}
}
