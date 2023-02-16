package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	//JPA는 SQL 쿼리를 사용하지 않고 JPA 메소드가 SQL 쿼리로 변환해서 처리
	
	//findAll()
	//findById()
	//save()	<== Insert, Update
	//delete()	<== delete
	
	
	// 사용자를 조회하는 기능이 필요하므로 findByusername 추가
	// 로그인 처리하기 위해서 사용자 정보를 입력받아서 DataBase에서 Select 해서 SiteUser객체에 저장함.
	// select * from site_user where username = ?(username아래의 인풋값)
	Optional<SiteUser> findByusername(String username);
		//로그인된 사용자의 정보를 SiteUser에 set주입한다.
}
