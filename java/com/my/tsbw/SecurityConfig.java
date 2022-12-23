package com.my.tsbw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.my.tsbw.user.WebUserSecurityService;
import com.my.tsbw.user.WebUserService;

import lombok.RequiredArgsConstructor;

/**
 * 스프링 시큐리티 규칙 적용 파일
 * @author Administrator
 *	- 인증, 권한 등에 관련된 설정, 빈(Java Been)등을 정의
 *  - @Configuration : 스프링의 환경설정 파일이다 의미
 *  - @EnableWebSecurity : 모든 요청 URL이 스프링 시큐리티의 제어를 받는다 (규칙(예외) 정의)
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

//@PreAuthorize("isAuthenticated()") 사용때문에 추가됨
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class SecurityConfig {
	// 스프링 시큐리티 세부 설정은 SecurityFilterChain 빈을 통해서 가능
	// 모든 인증되지 않는 요청을 허락한다
	// 로그인 하지 않아도 모든 페이지에 접근 가능하다
	// Bean
	@Bean
	public SecurityFilterChain fileterChain(HttpSecurity http) throws Exception
	{
		// 로그인 하지 않아도(인증되지 않아도) 모든 페이지에 접근 가능하다
		http.authorizeRequests().antMatchers("/**").permitAll()
		// h2-console 접근 ok ===================================================
		.and()
		.csrf().ignoringAntMatchers("/h2-console/**")
		// 본사이트에 다른 사이트 내용이 삽입되도록 허용하는 처리
		// X-Frame-Options 헤더값 적용 => 처리됨.
		.and()
		.headers()
		.addHeaderWriter(new XFrameOptionsHeaderWriter(
				XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
		// =======================================================================
		// 타임리프에서 replacet, insert 처리 때문에 추가
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		// =======================================================================
		// 보안 시큐리티에 로그인 URL 등록 
		.and()
		.formLogin()              // 로그인 폼 처리
		.loginPage("/user/login") // 로그인 url
		.defaultSuccessUrl("/")   // 로그인 성공후 홈페이지로 이동
		// =======================================================================
		// 로그아웃 처리
		.and()
		.logout()                    // 로그아웃
		.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃 주소 세팅
		.logoutSuccessUrl("/")       // 성공하면 홈페이지 이동
		.invalidateHttpSession(true) // 세션 유효하지 않게 처리
		
		;
		return http.build();
	}
	
	// 빈을 만들는 가장 쉬운방법 => @Configration으로 적용된 클레스에서 빈생성
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// WebUserSecurityService를 의존성 주입 처리
	// AuthenticationManager 빈 생성 => 스프링 시큐리티 내부에서 인증 처리 담당
	// WebUserSecurityService, 비밀번호 인코더 처리 자동으로, 설정되어서 처리된다
	private final WebUserSecurityService webUserSecurityService;
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration  authenticationConfiguration
			) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}




