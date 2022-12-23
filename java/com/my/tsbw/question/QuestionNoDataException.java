package com.my.tsbw.question;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 실제로는 => 해당 페이지 없다
@ResponseStatus( value=HttpStatus.NOT_FOUND, reason="Data not found" )
public class QuestionNoDataException extends RuntimeException {
	public QuestionNoDataException(String msg) {
		super(msg);
	}
}
