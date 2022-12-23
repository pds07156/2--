package com.my.tsbw.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebUserCreateForm {
	// 사용자 이름은 3~25 사이즈를 만족
	@Size(min=3 , max=25)
	@NotEmpty(message="사용자 이름(ID)은 필수 입력 항목입니다.")
	private String username;
	
	@NotEmpty(message="비밀번호는 필수 입력 항목입니다.")
	private String password1;
	
	@NotEmpty(message="비밀번호 확인은 필수 입력 항목입니다.")	
	private String password2;
	
	@NotEmpty(message="이메일은 필수 입력 항목입니다.")
	@Email
	private String email;
}
