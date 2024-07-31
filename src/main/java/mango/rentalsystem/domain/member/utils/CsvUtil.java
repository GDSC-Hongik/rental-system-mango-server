package mango.rentalsystem.domain.member.utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import org.springframework.stereotype.Component;


import mango.rentalsystem.domain.member.domain.Member;

@Component
public class CsvUtil {

	public List<Member> readMembersFromCsv(String filePath) {
		List<Member> members = new ArrayList<>();
		try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
			List<String[]> records = csvReader.readAll();
			for (String[] record : records) {
				String studentId = record[0]; // CSV파일에 학번,이름,전화번호 순으로 작성되어있다 가정
				String name = record[1];
				String phone = record[2];
				String password = record[2];

				Member member = Member.builder()
					.studentId(studentId)
					.name(name)
					.phone(phone)
					.password(password)
					.build();

				members.add(member);
			}
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
		return members;
	}
}