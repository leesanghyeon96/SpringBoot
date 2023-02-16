package com.mysite.sbb.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.DataNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/signup")
	 public String signup(UserCreateForm userCreateForm) {
		
		return "signup_form";
	 }
	

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		 				//유효성체크에서 문제가 생시면 Valid가 작동되어 뒤의 BindingResult로 던진다.
		if (bindingResult.hasErrors()) {
			return "signup_form";
		 }
		
		
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect",
					"2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		
		try {
		userService.create(userCreateForm.getUsername(), 
				userCreateForm.getEmail(), userCreateForm.getPassword1());
		
		}catch(DataIntegrityViolationException e){
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		}catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("singupFailed", e.getMessage());
			return "signup_form";
		}
		
		return "redirect:/";
	}
	
	//스프링 시큐리티에 로그인 URL 을 /user/login으로 설정했으므로 해당 매핑 추가
	//2월 16일 로그인 요청에 대한 login_form.html 뷰 페이지 전송
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
	//실제 로그인을 진행하는 @postMapping 방식의 메소드는 스프링 시큐리티가 대신 처리하므로
	//직접 구현할 필요가 없다.
	
	
	
	
}
