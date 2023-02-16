package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity		// JPA에서 자바객체를 DB의 테이블에 매핑
public class Answer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //Identity : 고유값을 넣겠다
	private Integer id;		//primary Key(Answer), 자동증가
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate;	//create_date
	
	@ManyToOne	//Question : one, question : Many
	private Question question;	//Question테이블(Fk)
		// Question(부모)테이블의 Primary Key를 참조(id)
		// Foreign key : 부모테이블의 PK, UK컬럼의 값을 참조해서 값을 할당
		// 질문 하나에 여러개의 답변이 올 수 있다.
		// question_id(Question테이블의 id컬럼) <= DB


	//여러개의 질문이 한명의 사용자에게 작성될 수 있으므로 @ManyToOne 관계가 성립된다.
	//답변정보
	@ManyToOne			//foreign key, SiteUser를 참조해서 값을 넣는다.
	private SiteUser author;

	//언제 수정되었는지 확인할 수 있도록
	private LocalDateTime modifyDate;










}
