package mango.rentalsystem.domain.member.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mango.rentalsystem.domain.member.domain.Member;

@Component
@RequiredArgsConstructor
public class CsvUtil {

	private final PasswordEncoder passwordEncoder;

	public List<Member> readMembersFromCsv(Member member, String filePath) {
		List<Member> members = new ArrayList<>();
		try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
			List<String[]> records = csvReader.readAll();
			for (String[] record : records) {
				String studentId = record[0]; // CSV파일에 학번,이름,전화번호 순으로 작성되어있다 가정
				String name = record[1];
				String phone = record[2];

				Member targetMember = Member.createMember(studentId, passwordEncoder.encode(phone), name,
					member.getDepartment(), phone);

				members.add(targetMember);
			}
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
		return members;
	}
}