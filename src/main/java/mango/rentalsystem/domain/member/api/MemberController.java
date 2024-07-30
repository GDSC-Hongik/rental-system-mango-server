package mango.rentalsystem.domain.member.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mango.rentalsystem.domain.member.application.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/load")
	public String loadMembers(@RequestParam String filePath) {
		memberService.saveMembersFromCsv(filePath);
		return "CSV파일로부터 로드 중";
	}
}
