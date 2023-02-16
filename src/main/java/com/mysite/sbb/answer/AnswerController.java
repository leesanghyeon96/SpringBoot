package com.mysite.sbb.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	//http://localhost:9292/answer/create/1 요청에 대한 답변글 등록 처리
	
	//SecurityConfig.java에서 @EnableMethodSecurity(prePostEnabled = true) 클래스 위에
	// 부여되어 있을때 아래의 @가 작동됨
	@PreAuthorize("isAuthenticated()")	//로그인시에만 접근
	@PostMapping("/create/{id}")	//위의 /answer가 내려와 붙는다.
	public String createAnswer(
			//<<validation 전 구성>>
			//Model model, @PathVariable("id") Integer id,@RequestParam String content
			
			//content의 유효성 검사
			Model model, @PathVariable("id") Integer id,	//@RequestParam으로 받는것이 아닌 validation을 통해 받는다.
			@Valid AnswerForm answerForm, BindingResult bindingResult, 
			Principal principal
			//2월 16일 추가 작성
			// 현재 로그인한 사용자에 대한 정보를 알기 위해서는 스프링 시큐리티가 제공하느 Principal 객체를 사용
			// principal.getName()을 호출하면 현재 로그인한 사용자의 사용자명(사용자ID)을 알 수 있다.
			) {
				// 위의 content <= form 에서 넘어오는 name값 (값이 여러개면 ,@Reque.. 로 )
		
		Question question = this.questionService.getQuestion(id);
		// 답변 내용을 저장하는 메소드 호출 (Service에서 호출)
		
		SiteUser siteUser = this.userService.getUser(principal.getName());
		
		
		
		//content의 값이 비어있을때
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";	//뷰페이지에 남아있도록 처리
		}
		
		//답변 내용을 저장하는 메소드 호출(service에서 호출)
		this.answerService.create(question, answerForm.getContent(), siteUser);
		
		
		return String.format("redirect:/question/detail/%s", id);
							// id변수를 %s에 주입
		//redirect : 클라이언트가 서버에게 재요청(View페이지가 아님)
			//본인페이지
	}
	
	//답변수정을 처리하는 get방식의 메소드 추가
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		
		Answer answer = this.answerService.getAnswer(id);
		
		if(!answer.getAuthor().getUsername().equals(principal.getName())){
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	
	//답변수정을 처리하는 메서드 추가
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
	//답변삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		
		Answer answer = this.answerService.getAnswer(id);
		
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		
		this.answerService.delete(answer);
		
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
	
	
	
	
	
	
	
}
