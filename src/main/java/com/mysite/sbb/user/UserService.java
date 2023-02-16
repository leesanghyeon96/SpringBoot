package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String username, String email, String password) {
		
		SiteUser user = new SiteUser();
		
		user.setUsername(username);
		
		user.setEmail(email);
		
		//BCryptPasswordEncoder는 BCrypt 해싱 함수를 사용해 비밀번호를 암호화한다.
		//여기서 new하는 것보다 빈등록을 사용하는것이 좋다.(인크루드와 비슷한 이유)
		//BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		//user.setPassword(passwordEncoder.encode(password));
		user.setPassword(passwordEncoder.encode(password));
		
		this.userRepository.save(user);
		
		
		return user;
	}
	
	
	//username을 인풋받아 사용자 정보 얻어오기
	public SiteUser getUser(String username) {
		
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		
		if(siteUser.isPresent()) {	//Optional에 SiteUser가 검색되면 (존재하면)
			return siteUser.get();
		}else {
			throw new DataNotFoundException("siteuser not found");
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
