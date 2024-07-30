package mango.rentalsystem.domain.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mango.rentalsystem.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	Member findByStudentId(String studentId);

}