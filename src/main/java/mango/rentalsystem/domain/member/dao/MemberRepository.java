package mango.rentalsystem.domain.member.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mango.rentalsystem.domain.department.domain.Department;
import mango.rentalsystem.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByStudentId(String studentId);

	List<Member> findAllByDepartment(Department department);
}

