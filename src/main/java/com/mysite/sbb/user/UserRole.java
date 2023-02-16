package com.mysite.sbb.user;
import lombok.Getter;

@Getter	//상수 자료형이므로 @Setter없이 @Getter만 사용
public enum UserRole {	//enum으로 작성
	//스프링 시큐리티는 인증뿐 아니라 권한도 관리한다.
	//인증후에 사용자에게 부여할 권한이 필요하다.
	//ADMIN, USER 2개의 권한을 갖는 UserRole을 작성
	
	//열거된 내용중 하나만 
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	UserRole(String value){
		this.value = value;
	}
	
	
	private String value;
}
