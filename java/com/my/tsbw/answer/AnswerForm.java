package com.my.tsbw.answer;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
	@NotEmpty(message="내용은 필수 입력 항목입니다.")
	private String content;
}
