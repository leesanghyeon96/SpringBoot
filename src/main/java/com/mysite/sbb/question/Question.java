package com.mysite.sbb.question;

import java.time.LocalDateTime;	//자신의 시스템의 로컬의 시간설정
import java.util.List;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.CascadeType;	
import jakarta.persistence.Column;
//persistence : JPA에서 사용된 어노테이션
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Entity 클래스 : DB에 클래스이름이 없으면 그 테이블을 새로 만든다.
@ToString
@Getter
@Setter
@Entity		//자바 클래스를 DataBase의 테이블과 매핑된 클래스 : 테이블명 : qeustion
public class Question {	//클래스이름 : 테이블이름
	// 필드의 이름 : 테이블 컬럼의 이름
	@Id	//primary key : JPA를 활성화시키려면 반드시 PK컬럼이 있어야한다.
	@GeneratedValue (strategy = GenerationType.IDENTITY)	//시퀀시 할당
	//(strategy = GenerationType.IDENTITY) 컬럼의 값 자동증가
	private Integer id; //Primary Key(Question), 시퀀스 (1, 1)
	
	@Column(length =200)		//200자까지 
	private String subject;
	@Column(columnDefinition = "TEXT")	//TEXT : 최대2GB까지 저장가능
	private String content;
	
	private LocalDateTime createDate;	//create_date:DB
	/*
	@Column(length = 300)	//컬럼추가
	private String addr;
	*/
	
	//Question 테이블에서 Answer 테이블을 참조하는 컬럼을 생성 @onetoMany
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
		//cascade = CascadeType.REMOVE : 부모테이블의 레코드가 삭제되면 참조테이블도 삭제 되도록
	private List<Answer> answerList;	//question객체에 맵핑된 Answer정보를 가지고있다.
		// List<Answer> : 타입 , answerList : 변수
		// 여러개의 답변을 List에 담는다.
		// question.getAnswerList(); <= 값 가져오기
		// Answer의 question 컬럼과 반대
		// 질문에 대한 답변을 게더링하기위해
	//one : question
	//many : answer테이블
	//answerList에는 question으로 매핑된 Answer테이블의 값을 가지고있다.
	//id값의 answer객체정보 저장됨
	
	
	//여러개의 질문이 한명의 사용자에게 작성될 수 있으므로 @ManyToOne관계가 성립한다.
	@ManyToOne	// Foreign key : site_user 테이블의 Primary Key 참조
	private SiteUser author;
	
	//언제 수정되었는지 확인할 수 있도록
	private LocalDateTime modifyDate;
	
	
	
	
	
	
}
