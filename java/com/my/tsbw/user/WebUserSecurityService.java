package com.my.tsbw.user;

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

@RequiredArgsConstructor
@Service
public class WebUserSecurityService implements UserDetailsService{
	private final WebUserRepository webUserRepository;
	
	// 스프링 시큐리티는 loadUserByUsername() 메소드가 리턴하는 User의 
	// 비번정보를 화면에서 입력받은 정보와 비교하여 일치하는치 점검한다(내부적으로)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 1. 회원정보 획득 By 회원이름(ID)
		Optional<WebUser> _user = webUserRepository.findByName( username );
		if(_user.isEmpty()) {
			throw new UsernameNotFoundException("사용자가 없습니다.");
		}
		// 2. 회원 맞으면 권한 부여(나름대로 규칙에 의해)
		WebUser u = _user.get();
		// 권한을 담을 리스트
		List<GrantedAuthority> authos = new ArrayList<>();
		// 설정 : 아이디가 admin이면 ADMIN 부여 아니면 다 USER 부여
		if( u.getName().equals("admin")) {
			authos.add(new SimpleGrantedAuthority( WebUserRole.ADMIN.getValue() )); // 권한 추가
		}else {
			authos.add(new SimpleGrantedAuthority( WebUserRole.USER.getValue() ));  // 권한 추가
		}
		
		// 3. 내부적으로 관리하는 User객체 생성 리턴
		return new User(u.getName(), u.getPassword(), authos);
	}

}
