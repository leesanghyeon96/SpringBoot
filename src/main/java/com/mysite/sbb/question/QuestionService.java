package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor	// DI(생성자에 객체주입)
@Service	// 해야 빈등록
public class QuestionService {
	//JPA 메소드를 사용하기 위해 (생성자를 이용한 객체 자동 주입)
	private final QuestionRepository questionRepository;
	
	//메소드선언 : question 테이블의 List정보를 가지고오는 메소드 <02월 04일 수정됨 : Paging 처리를 위해>
//	public List<Question> getList(){
//		return this.questionRepository.findAll();
//			//questionRepository : DB에 접근하도록 설정되어있음
//	}
	
	//Controller에서 getList메소드 호출시 출력할 page번호를 매개변수로 받음. : 0, 1, 2, 3
	public Page<Question> getList(int page, String kw){ //2월 17일 String kw추가(검색어를 의미함)
		
		//최신글을 먼저 출력하기, 날짜 컬럼 (createDate) 을 desc해서 출력
		List<Sort.Order> sorts = new ArrayList();
		sorts.add(Sort.Order.desc("createDate"));
		
		//Pageable 객체에 2개의 값을 담아서 매개변수로 던짐 , 10 <= 출력할 레코드 수
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		
		//2월 17일 추가
		//Specification<Question> spec = search(kw);
		//2월 17일 삭제 : findAllByKeyWord 메소드를 사용하기위해
		
		
		//2월 17일 수정
		//return this.questionRepository.findAll(spec, pageable);
		//2월 17일 재수정 : findAllByKeyWord 메소드를 사용
		return this.questionRepository.findAllByKeyword(kw, pageable);
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
	
	//insert라 void(리턴필요X)
	//2월 16일 SiteUser user 추가
	public void create(String subject, String content, SiteUser user) {
		//Question 객체를 생성후 Setter주입
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		
		q.setAuthor(user);	//user객체에 pk값이 들어간다.
		
		//레파지토리 객체(save())를 호출해서 내용을 저장
		this.questionRepository.save(q);	//Db에 insert
		
	}
	
	
	//2월 16일 글 수정 항목 추가됨
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		
		this.questionRepository.save(question);
	}
	
	//2월 16일 질문 삭제기능 추가
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	//2월 17일 추천인을 저장하기 위한 메소드 생성
	public void vote(Question question, SiteUser siteUser) {
		// question.getVoter() : 테이블의 set
		question.getVoter().add(siteUser);
			//set<SiteUesr> voter 에 siteUser를 넣는다.
		this.questionRepository.save(question);
	}
	
	
	//2월 17일 검색 메소드 추가
	private Specification<Question> search(String kw){
		
		return new Specification<>() {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);	//중복제거
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Question, SiteUser> u2 = q.join("author", JoinType.LEFT);
				
				 return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						 cb.like(q.get("content"), "%" + kw + "%"), // 내용
						 cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						 cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						 cb.like(u2.get("username"), "%" + kw + "%")); // 답변 작성자
			}
		};
	}
	
	
}
