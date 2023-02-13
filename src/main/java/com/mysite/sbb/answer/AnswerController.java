package com.mysite.sbb.answer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	//http://localhost:9292/answer/create/1 요청에 대한 답변글 등록 처리
	@PostMapping("/create/{id}")	//위의 /answer가 내려와 붙는다.
	public String createAnswer(
			//<<validation 전 구성>>
			//Model model, @PathVariable("id") Integer id,@RequestParam String content
			
			//content의 유효성 검사
			Model model, @PathVariable("id") Integer id,	//@RequestParam으로 받는것이 아닌 validation을 통해 받는다.
			@Valid AnswerForm answerForm, BindingResult bindingResult
			) {
				// 위의 content <= form 에서 넘어오는 name값 (값이 여러개면 ,@Reque.. 로 )
		
		Question question = this.questionService.getQuestion(id);
		// 답변 내용을 저장하는 메소드 호출 (Service에서 호출)
		
		//content의 값이 비어있을때
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";	//뷰페이지에 남아있도록 처리
		}
		
		
		this.answerService.create(question, answerForm.getContent());
		
		
		return String.format("redirect:/question/detail/%s", id);
							// id변수를 %s에 주입
		//redirect : 클라이언트가 서버에게 재요청(View페이지가 아님)
			//본인페이지
	}
	
	
	
	
	
	
	
}
