package com.mysite.sbb.question;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor	// DI(생성자에 객체주입)
@Service
public class QuestionService {
	//JPA 메소드를 사용하기 위해 (생성자를 이용한 객체 자동 주입)
	private final QuestionRepository questionRepository;
	
	//메소드선언 : question 테이블의 List정보를 가지고오는 메소드
	public List<Question> getList(){
		return this.questionRepository.findAll();
	}
	
	//상세 페이지를 처리하는 메소드 : id를 받아서 Question 테이블을 select(findById(1))
		//해서 select한 레코드를 Question 객체에 담아서 리턴
	public Question getQuestion(Integer id) {
		 //select * from question where id = ?
		Optional<Question> op = this.questionRepository.findById(id);
		if(op.isPresent()) {	//op에 값이 존재하는 경우
			return op.get();	//Question 객체를 리턴
		}else {
			//사용자정의 예외 (sbb 아래에 클래스를 생성해 임포트 시키기.)
			//throw : 예외를 강제로 발생시킨다.
			//throws : 예외를 요청한 곳에서 처리 하도록 미루는것
			throw new DataNotFoundException("요청한 파일을 찾지 못했습니다.");
		}
	}
	
}
