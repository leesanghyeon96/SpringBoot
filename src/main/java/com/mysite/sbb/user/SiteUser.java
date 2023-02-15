package com.mysite.sbb.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class SiteUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//unique 키를 h2-console에 적용시키려면 콘솔에서 drop이후 다시 만들면 나온다.
	@Column(unique = true)	
	private String username;
	
	private String password;
	
	@Column(unique = true)
	private String email;
	
	
}
