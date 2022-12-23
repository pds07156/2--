package com.my.tsbw.user;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.my.tsbw.answer.AnswerRepository;
import com.my.tsbw.question.QuestionNoDataException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WebUserService {
	private final WebUserRepository webUserRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 회원가입처리
	public void create(String name, String password, String email){
		// 1. WebUser 객체 생성
		WebUser user = new WebUser();
		// 2. 데이터 세팅 => 비밀번호는 노출되면 않된다!! => 암호화
		user.setName(name);
		user.setEmail(email);
		// 암호화, BCrypt 해싱 함수 사용하여 비밀번호 암호화
		// BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// BCryptPasswordEncoder 객체를 new로 생성하는 방식보다는 bean으로 관리하는 방식 선호
		// BCryptPasswordEncoder 객체를 자바빈으로 등록해서 사용(권장) -> SecurityConfig 
		user.setPassword( passwordEncoder.encode(password) );
		// 3. 레포티토리.save( WebUser객체 )
		webUserRepository.save( user );		
	}
	
	// 사용자 이름 넣으면 WebUser 객체 리턴
	public WebUser getUser(String username) {
		Optional<WebUser> user = webUserRepository.findByName(username);
		if( user.isPresent() ) {
			return user.get();
		}
		// throw new DataNotFoundException("user not found"); 
		throw new QuestionNoDataException("user not found");
	}
	
	
	// 로그인	
	// 로그아웃
}
