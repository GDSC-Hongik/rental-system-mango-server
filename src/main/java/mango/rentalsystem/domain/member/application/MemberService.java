package mango.rentalsystem.domain.member.application;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mango.rentalsystem.domain.member.dao.MemberRepository;
import mango.rentalsystem.domain.member.domain.Member;
import mango.rentalsystem.domain.member.utils.CsvUtil;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CsvUtil csvUtil;

	public void saveMembersFromCsv(String filePath) {
		List<Member> members = csvUtil.readMembersFromCsv(filePath);
		for (Member member : members) {
			// studentId로 중복 확인
			Optional<Member> existingMember = memberRepository.findByStudentId(member.getStudentId());
			if (!existingMember.isPresent()) {
				// 중복이 없다면 memberRepository에 새 member 추가
				memberRepository.save(member);
			}
		}
	}
}
