package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//User의 인증처리하는 서비스
//사용자 정보를 통해서 넘겨 받아서 인증을 처리함.

@RequiredArgsConstructor
@Service	
public class UserSecurityService implements UserDetailsService{
	//UserDetailsService : 인터페이스  <=에서 인증처리함
	//그래서 override
	//UserDetailsService를 구현해 UserSecurityService를 만든다.
	//그럼 프레임워크에서 처리해준다.
	
	private final UserRepository userRepository;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
										//form에서 넘기는(frame work에서 처리) username
		
		Optional<SiteUser> _siteUser = 
		this.userRepository.findByusername(username);
		//_siteUser(Optional)에 레코드의 값을 담는다.
		
		if(_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		//폼에서 넘어오는 usename을 DB에서 쿼리해서 siteUser객체에 담은 값을 Optional에서 뽑아옴
		// siteUser : DB에서 select해 가져온 레코드
		// siteUser에 값이 있으면 꺼내서 siteUser에 담는다.
		SiteUser siteUser = _siteUser.get();
		
		
		//허가정보를 담는 객체
		//GrantedAuthority, SimpleGrantedAuthority : 프레임워크에 담겨있다.
		//Authentication (인증) : Identity (ID) + Password를 확인하는것
		//Authorization  (허가) : 인증된 사용자에게 사이트의 서비스를 쓸 수 있도록 권한을 부여하는것
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
			//계정이 admin이면 ADMIN Role을 적용
		}else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
			//계정이 admin이 아니라면 USER Role을 적용
		}
		
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
			
	}
	
	/*
	 
	 스프링 시큐리티에 등록하여 사용할 UserSecurityService는 스프링 시큐리티가 제공하는 
	 UserDetailsService 인터페이스를 구현(implements)해야 한다. 
	 스프링 시큐리티의 UserDetailsService는 loadUserByUsername 메서드를 구현하도록 
	 강제하는 인터페이스이다. loadUserByUsername 메서드는 사용자명으로 비밀번호를 조회하여 리턴하는 메서드이다.
	
	 UserSecurityService는 스프링 시큐리티 로그인 처리의 핵심 부분이다.
	
	 조금 더 자세히 살펴보면, loadUserByUsername 메서드는 사용자명으로 
	 SiteUser 객체를 조회하고 만약 사용자명에 해당하는 데이터가 없을 경우에는 
	 UsernameNotFoundException 오류를 내게 했다. 
	 그리고 사용자명이 "admin"인 경우에는 ADMIN 권한을 부여하고 그 이외의 경우에는 USER 권한을 부여했다. 
	 그리고 사용자명, 비밀번호, 권한을 입력으로 스프링 시큐리티의 User 객체를 생성하여 리턴했다. 
	 스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된 User 객체의 비밀번호가 
	 화면으로부터 입력 받은 비밀번호와 일치하는지를 검사하는 로직을 내부적으로 가지고 있다. 
	 
	 */
	
	
	
	
	
	
	
	
	
	
}
