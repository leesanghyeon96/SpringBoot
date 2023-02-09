package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;

@SpringBootTest
class SbbApplicationTests {
	
	@Autowired	//객체 자동 주입 , JPA의 CRUD할 수 있는 메소드가 적용되어있음.
	private QuestionRepository questionRepository;	//@Autowired로 questionRepository활성화
	@Autowired	//객체 자동 주입 (DI) , JPA의 메소드를 사용, findAll(), findById(), save(), delete()
	private AnswerRepository answerRepository;
	
	/* 하나의 질문에 여러개의 답변 찾기 */
	@Transactional //org에있는것으로 임포트
		//JUnit에서 오류가발생(실제환경에서는 문제가 없다.) <test에서만 발생
		//아래의 메소드가 하나의 트랜잭션으로 작동되도록 설정(Test) - 2번이 함께작동되도록
	@Test
	public void testjpa8() {
		//1. Question 테이블에서 질문의 레코드를 얻어온다. 끄집어낸다.
		Optional<Question> op =
		this.questionRepository.findById(1);
		
		Question q = null;
		if(op.isPresent()) {
			q = op.get();	// Question테이블의 id 1번값이 저장
		}
		
		System.out.println(q.getId());
		System.out.println(q.getContent());
		System.out.println(q.getSubject());
		//@Transactional을 처리하지않으면 여기서 끊기고 다시 아래가 작동된다.
		//@Transactional를 써서 하나로 처리해준다.
		
		//2. 끄집어낸 객체의 q.getAnswerList(); <== 끄집어낸 객체의 답변글을 얻어온다.
		List<Answer> all =
		q.getAnswerList();	// <= question객체의 answer정보가 저장됨
			//Question 객체의 AnswerList 컬럼은 List<answer>
		
		//3. 출력구문에서 출력한다.
		for (int i = 0 ; i < all.size() ; i++) {
			Answer a = all.get(i);
			System.out.println(a.getId());
			System.out.println(a.getContent());
			System.out.println(a.getCreateDate());
			System.out.println("=======================");
		}
	}
	
	
	/* 답변 레코드 하나 가져오기 
	@Test
	public void testjpa7() {
		Optional<Answer> op =
		this.answerRepository.findById(2);
		
		if(op.isPresent()) {	//isPresent() : null->false, not null->true
			Answer a = op.get();
			System.out.println(a.getId());
			System.out.println(a.getContent());
			System.out.println(a.getCreateDate());
			//System.out.println(a.getQuestion());
		}
	}
	*/
	
	/* Answer 테이블에 Insert처리 
	@Test
	public void testAnswerjpa() {
		//1. Question(부모) 테이블의 답변을 처리할 레코드를 먼저 select 한다. findByid(1)
		Optional<Question> op = 
		this.questionRepository.findById(3);
		Question q = op.get();	//레코드 가져와 q에 담기
		//2. Answer 엔티티 테이블의 객체 생성을 하고, setter를 사용해서 값을 넣는다.
			// setQuestion(q)
		Answer a = new Answer();
		a.setQuestion(q);	// 질문
		a.setContent("3 - sbb답변입니다.");	// 답변
		a.setCreateDate(LocalDateTime.now());
		//3. save 메소드를 사용해서 저장
		this.answerRepository.save(a);
	}
	*/
	
	/*=====아래 : Question.java=============================*/
	
	/* 데이터 삭제 : JPA 메소드 : delete() 
	@Test
	public void testjpa6() {
		//1. 테이블에서 삭제할 레코드를 가지고 옴
		Optional<Question> op = 
		this.questionRepository.findById(2);
		//2. Optional에 저장된 객체를 끄집어낸다.
		Question q = op.get();
		//3. delete(q)
		this.questionRepository.delete(q);
	}
	*/
	
	/* 데이터 수정 : save() 
	@Test
	public void testjpa5() {
		//1. 수정할 객체를 findById() 메소드를 사용해서 가지고온다. 
		Optional<Question> op =
		this.questionRepository.findById(1);
		//2. 가지고온 객체를 끄집어내서 setter를 사용해서 수정 
		Question q = op.get();	//하나의 값이기 때문에 번호저장 안해도됨
		q.setSubject("수정된 제목");
		q.setContent("수정된 내용");
		//3. 수정된 객체를 save(q)
		this.questionRepository.save(q);
	}
	*/
	
	/* 테이블의 모든 레코드 정렬 : asc, desc 
	@Test
	public void testjpa4() {
		List<Question> all =
		this.questionRepository.findAllByOrderByCreateDateAsc();
		List<Question> all2 =
		this.questionRepository.findAllByOrderByCreateDateDesc();
		
		
		System.out.println("총 리스트에 있는 객체수 : " + all.size());
		System.out.println("=====ASC 정렬후 출력=======");
		for(int i = 0 ; i < all.size() ; i++) {
			Question q = all.get(i);
			
			System.out.println(q.getId());
			System.out.println(q.getContent());
			System.out.println(q.getSubject());
			System.out.println(q.getCreateDate());
			System.out.println("==========================");
		}
		System.out.println("====DESC 정렬후 출력==============");
		for(int i = 0 ; i < all2.size() ; i++) {
			Question q = all2.get(i);
			
			System.out.println(q.getId());
			System.out.println(q.getContent());
			System.out.println(q.getSubject());
			System.out.println(q.getCreateDate());
			System.out.println("==========================");
		}
	}
	*/
	
	/* 특정 조건을 사용해서 검색후 정렬해서 출력 
	@Test
	public void testjpa3() {
		List<Question> or =
		this.questionRepository.findBySubjectLikeOrderByCreateDateAsc("%sbb%");
			//시간을 기준으로
		List<Question> or2 =
		this.questionRepository.findBySubjectLikeOrderByCreateDateDesc("%sbb%");
		
		
		System.out.println("ASC : 오름차순 정렬");
		for(int i = 0 ; i < or.size() ; i++) {
		Question q = or.get(i);
		
		System.out.println(q.getId());
		System.out.println(q.getContent());
		System.out.println(q.getSubject());
		System.out.println(q.getCreateDate());
		System.out.println("============================");
		}
		System.out.println("======DESC : 내림차순 정렬======");
		for(int i = 0 ; i < or.size() ; i++) {
		Question q = or2.get(i);
		
		System.out.println(q.getId());
		System.out.println(q.getContent());
		System.out.println(q.getSubject());
		System.out.println(q.getCreateDate());
		}
	}
	*/
	
	/* 두 컬럼을 or 연산으로 검색 : subject , content 
	@Test
	public void testjpa2(){
		List<Question> sq =
		this.questionRepository.findBySubjectLikeOrContentLike("%sbb%", "%생성%");
		
		Question q4 = sq.get(0);
		System.out.println("리스트에 검색된 레코드 수 : " + sq.size());
		System.out.println(q4.getId());
		System.out.println(q4.getContent());
		System.out.println(q4.getSubject());
	}
	*/
	
	/* 사용자 정의 select 문 : subject 컬럼, content 컬럼 검색 , Like 
	@Test
	public void testjpa() {							//.findBySubjectLike(매개변수)
		List<Question> all = this.questionRepository.findBySubjectLike("%sbb%");
		
		Question q = all.get(0);
		System.out.println("리스트에 검색된 레코드 수 :" + all.size());
		System.out.println(q.getId());
		System.out.println(q.getSubject());
		System.out.println(q.getContent());
		
		System.out.println("=============================");
		
		List<Question> all2 = this.questionRepository.findByContentLike("%생성%");
		
		Question q3 = all2.get(0);
		System.out.println("리스트에 검색된 레코드 수 :" + all.size());
		System.out.println(q3.getId());
		System.out.println(q3.getSubject());
		System.out.println(q3.getContent());
	}
	*/
	
	/* 조건에 맞는 레코드 하나만 가져오기 : PK 컬럼은 findById(1)기본제공
	   Question 테이블의 Primary Key 컬럼은 기본적으로 제공됨 : findBy컬럼명()
	 	그 외의 컬럼은 조건을 설정해줘야한다.
	  	
	@Test
	public void jpaTestget() {	
		Optional<Question> oq = this.questionRepository.findById(1);
				//findById : 타입 Optional <= pk만 사용가능
		if(oq.isPresent()) {
			Question q = oq.get();
			System.out.println(q.getId());
			System.out.println(q.getSubject());
			System.out.println(q.getContent());
			System.out.println(q.getCreateDate());
		}
	}
	*/
	
	/* Select List JUnit Test 
	@Test
	public void jpaTest() {
		List<Question> all = this.questionRepository.findAll();
			//findAll() : DB의 모든 레코드를 가져와 question객체에 담아 List에 저장 
		//assertEquals(4, all.size());	// assertEquals(기대값, 실제값) , 성공(두 값이 일치)
					//all.size() : 레코드 개수
		Question q = all.get(1);	//List all 변수에 담긴 0번방의 Question 객체를 끄집어낸다.
		//assertEquals("sbb가 무엇인가요",q.getSubject());	//성공
		
		System.out.println(q.getId());
		System.out.println(q.getSubject());
		System.out.println(q.getContent());
	}
	*/
	
	/* Insert JUnit Test , JPA 인터페이스에 정의된 save() 
	@Test
	void contextLoads() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요");
		q1.setContent("sbb에 대해서 알고십습니다.");
		q1.setCreateDate(LocalDateTime.now());	//현재시간을 setter에 저장
		this.questionRepository.save(q1);	//첫번째 질문 저장(쿼리 자동실행)
		
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요. ");
		q2.setCreateDate(LocalDateTime.now());	//현재시간을 setter에 저장
		this.questionRepository.save(q2);	
		
		// 테스트 => run as => jUnit test
		// devtool과 run as 를 동시에 하면 오류가 난다.
		// devtools중지->Sbb jUnit test->성공시devtools작동->H2에서테이블확인
		// 테스트 후 devtools를 중지(1개씩)
		
	}
	*/
}
