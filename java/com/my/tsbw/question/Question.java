package com.my.tsbw.question;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.my.tsbw.answer.Answer;
import com.my.tsbw.user.WebUser;

import lombok.Getter;
import lombok.Setter;

/**
 * Question 엔티티 역활 담당
 * @author Administrator
 *
 */
@Getter
@Setter
// JPA의 엔티티가 되었다, 인식한다
@Entity
public class Question {
	// primary key
	// @Id => 기본키로 지정한다, 중복 X, 각 데이터를 구분하는 유효한 값
	// 자동 증가 : AUTO_increment => 별도 세팅 없이 1씩 자동 증가(데이터추가되면)
	// strategy : 고유번호 생성 옵션
	// GenerationType.IDENTITY : 해당 컬럼의 고유한 시퀀스 생성=>번호증가시 사용
	@Id	
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Integer id;
	
	// @Column : 엔티티의 속성을 컬럼명과 일치시킨다 
	@Column(length = 128)
	private String subject;
	
	// 컬럼 속성 : columnDefinition
	@Column( columnDefinition = "TEXT" )
	private String content;
	
	// createDate(카멜표기법 사용) -> create_date (실제 컬럼명)
	private LocalDateTime createDate;
	
	
	// 질문에 종속된 모든 답변들
	// mappedBy 값은 참조된 엔티티(Answer)의 특정 속성명
	@OneToMany(mappedBy="question", cascade=CascadeType.REMOVE)
	// cascade  : 질문 삭제되면 답변도 다 날릴것인가?
	private List<Answer> answerList;
	
	// 여러개의 질문 한명이 작성할수 있다!!
	@ManyToOne
	private WebUser author;
	
	// 수정일
	private LocalDateTime modifyDate;
}






