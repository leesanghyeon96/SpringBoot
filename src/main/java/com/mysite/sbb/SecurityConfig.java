package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration	// security의 설정을 적용하는 어노테이션
@EnableWebSecurity	//모든 요청 URL이 스프링 시큐리티의 제어를 받도록 하는 어노테이션
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// 아래의 코드는 인증되지 않은 요청을 허락한다는 의미
		http.authorizeHttpRequests().requestMatchers(
				 new AntPathRequestMatcher("/**")).permitAll()
			
			//아래는 스프링 시큐리티가 CSRF처리시 H2 콘솔은 예외로 처리할 수 있도록 처리
			.and()	// http 객체의 설정을 이어서 할 수 있게 하는 메소드
				.csrf().ignoringRequestMatchers(
						new AntPathRequestMatcher("/h2-console/**"))
						//h2-console/로 시작하는 URL은 CSRF검증을 하지 않는다는 설정
		
			.and()
				.headers()
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
						XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
				
				;
		return http.build();
	}
	
	@Bean	//아래의 PasswordEncoder는 인터페이스이다.
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
