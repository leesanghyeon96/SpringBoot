package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
	// <Question, Integer>
	// Question : CRUD할(DAO) 클래스(entity) 이름 , Integer : Question클래스의 pk 컬럼의 데이터 타입
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	// JPA에서 Question 테이블을 Select, Insert, Update, Delete (DAO)
	// JPA의 CRUD메소드 : 메소드사용 => DB에 맞는 쿼리를 자동으로 처리
		// save() : Insert, Update, Delete
		// findAll()
		// findById()
		// 정의해서 사용 : findBysubject()
	
	// Question 테이블을 SQL쿼리를 사용하지 않고 JPA메소드를 사용해서 CRUD하는 Repository
		//JpaRepository<Question, Integer>
			//Question : JPA 메소드에서 쿼리할 엔티티 클래스
			//Integer : 엔티티 클래스의 Primary key 컬럼의 데이터 타입
	
	// 하나의 컬럼을 조건으로 처리된 값 가져오기 (findBy = where)
	// findById() : Question 테이블의 Primary Key 컬럼이므로 기본 제공됨
	
	// PK,Uk컬럼이 아닌 다른 컬럼을 기준으로 가져오는 방법(JPA메소드를사용해쿼리생성)
	//subject 기준으로 검색시 정의	
	Question findBySubject(String subject);	//<= 검색 결과가 1개일때 처리
		// select * from question where subject = ?(String subject)
	//content 기준으로 검색시 정의
	Question findByContent(String content);	//<= 검색 결과가 1개일때 처리
		// select * from question where content = ?(String content)
	
	// 값이 여러개일때 List에 담아줘야한다.
	List<Question> findBySubjectLike(String subject); //대소문자주의
		//select * from question where subject like '%sbb%'
	
	//content 컬럼을 조건으로 검색
	List<Question> findByContentLike(String content);
		//select * from question where content like '%내용%'
	
	// or조건으로 검색
	List<Question> findBySubjectLikeOrContentLike(String subject,String content);
		//select * from question where subject like '%sbb%' or content like '%내용%'
	
	// 조건을 사용해서 출력후 정렬해서 출력 : Order By
	List<Question> findBySubjectLikeOrderByCreateDateAsc(String subject);
		//select * from question where subject = ? order by all asc;	: 오름차순정렬
	List<Question> findBySubjectLikeOrderByCreateDateDesc(String subject);	
		//select * from question order by all desc;	: 내림차순정렬
	
	// 모든 레코드를 정렬해서 출력
	List<Question> findAllByOrderByCreateDateAsc(); //CreateDate(앤티티)
		//select * from question order by create_date(실제티이블) asc
	List<Question> findAllByOrderByCreateDateDesc(); //CreateDate(앤티티)
		//select * from question order by create_date(실제티이블) desc
	
	// Update : save()
	
	// Delete : delete()
	
	// 페이징을 처리하기 위한 메소드 생성
	// select * from question : Pageable 편수에 : page, 레코드수를 넣어주면(10)
	// JPA에서 페이지 번호는 0번부터 시작
	// 출력할 레코드수를 JPA에 알려주면 내부적으로 JPA가 전체 레코드(1000) / 10 = 100페이지가 나온다.
	Page<Question> findAll(Pageable pageable);
	// page는 list와 비슷하다.
	
}
