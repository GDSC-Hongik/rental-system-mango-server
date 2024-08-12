package mango.rentalsystem.domain.rental.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.rental.domain.Rental;
import mango.rentalsystem.domain.rental.domain.RentalStatus;

public interface RentalRepository extends CrudRepository<Rental, Long> {

	List<Rental> findAllByMember(Member member);

	List<Rental> findAllByMemberDepartment(Department department);

	List<Rental> findAllByRentalStatusAndDeadlineDateTimeBefore(RentalStatus rentalStatus, LocalDateTime localDateTime);
}
