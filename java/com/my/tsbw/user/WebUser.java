package com.my.tsbw.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.my.tsbw.answer.Answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WebUser {
	// 요구사항에 없더라고, 기본적으로 입력
	@Id	
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	// Integer : -2^(32-1) ~ 0 ~ 2^(32-1)-1
	// Long    : -2^(64-1) ~ 0 ~ 2^(64-1)-1	
	private Long id;
	
	@Column(unique=true)
	private String name;
	
	private String password;
	@Column(unique=true)
	private String email;
}
