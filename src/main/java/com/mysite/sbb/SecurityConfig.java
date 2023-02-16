package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration	// security의 설정을 적용하는 어노테이션
@EnableWebSecurity	//모든 요청 URL이 스프링 시큐리티의 제어를 받도록 하는 어노테이션
@EnableMethodSecurity(prePostEnabled = true)
	//SecurityConfig에 적용한 @EnableMethodSecurity 애너테이션의 prePostEnabled = true 
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// 아래의 코드는 인증되지 않은 요청을 허락한다는 의미
		// Spring Security 에서 모든 URL에서 인증없이 접근할 수 있도록 허용함
		http.authorizeHttpRequests().requestMatchers(
				 new AntPathRequestMatcher("/**")).permitAll()
			
			//아래는 스프링 시큐리티가 CSRF처리시 H2 콘솔은 예외로 처리할 수 있도록 처리
			//H2 DataBase는 Http로 통신하는 DataBase이므로 csrf적용되지 않도록 설정
			.and()	// http 객체의 설정을 이어서 할 수 있게 하는 메소드
				.csrf().ignoringRequestMatchers(
				// form태그 내에서 action이 들어가면 csrf인증키를 자동으로 발급됨(hidden)
				// 외부의 접근을 방지
						new AntPathRequestMatcher("/h2-console/**"))
						//h2-console/로 시작하는 URL은 CSRF검증을 하지 않는다는 설정
			
			//아래도 h2에서 접근이 가능하도록 설정
			.and()
				.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
			
			//로그인과 로그아웃은 컨트롤러에서 처리하지 않고 스프링 시큐리티가 처리해준다.
			//스프링 시큐리티의 로그인 설정을 담당하는 부분
				//로그인을 자동(프레임워크에서)으로 처리해줌
			.and()
				.formLogin()
				.loginPage("/user/login") ///user/login으로 요청이 들어오면
					//form에서 넘어오는 값이 위에 담겨 처리됨
				.defaultSuccessUrl("/") //로그인 성공시 세션(서버 메모리)에 로그인 정보를 담고 / 페이지로 이동
					//로그인 성공시 리다이렉션할 페이지
			//스프링 시큐리티 로그아웃 처리부분
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true) //세션의 담긴 모든 값을 삭제해라
				
			;	
				
		return http.build();
	}
	
	@Bean	//아래의 PasswordEncoder는 인터페이스이다.
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//스프링 시큐리티의 인증을 담당
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*
	 AuthenticationManager 빈을 생성했다. 
     AuthenticationManager는 스프링 시큐리티의 인증을 담당한다. 
     AuthenticationManager 빈 생성시 스프링의 내부 동작으로 인해 위에서 작성한 
     UserSecurityService와 PasswordEncoder가 자동으로 설정된다.
	 */
	
}