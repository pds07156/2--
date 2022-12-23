package com.my.tsbw.question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * => 나중에 JavaDoc 만드는 주석법도 정리
 * @author Administrator
 * 	- html 입력폼에서 받는 필드를 구성
 *
 */
@Getter
@Setter
public class QuestionForm {	
	// 제목
	// @NotEmpty => 빈 문자열:""을 허용하지 않는다
	@NotEmpty(message="제목은 필수 입력 항목입니다.")
	// 최대 길이가 128 Byte를 넘으면 않된다
	@Size(max=128)
	private String subject;
	
	// 내용
	@NotEmpty(message="내용은 필수 입력 항목입니다.")
	private String content;
}
