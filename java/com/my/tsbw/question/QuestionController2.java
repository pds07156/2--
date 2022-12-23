package com.my.tsbw.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class QuestionController2 { 
	/**
	 * 스프링에서 의존성 주입(Dependency Injection) 방식 3
	 * - @Autowired => 단위 테스트에서 사용해봄
	 * - 생성자 - 생성자를 작성하여 객체를 주입하는 방식 ( this.xxx = xxx;) => 권장
	 * - Setter - Setter 메소드를 만들어서 객체를 주입하는 방식
	 */
	private final QuestionRepository questionRepository;
	
	@RequestMapping("/question/todoList2")
	// @ResponseBody이 없으면 templates  밑에서 question_view.html을 찾는다
	//@ResponseBody
	// 템플릿 파일이 없으면 -> 500 에러 -> 템플릿이 없다 물리적으로
	// Model 객체는 자바 클레스와 템플릿 간에 연결 고리
	// 함수의 파라미터로 모델 객체를 사용 => 데이터를 세팅해서 템플릿에 전달가능
	public String todoList2(Model model) {
		// 질문 목록을 디비에서 다 가져와서, 화면에 뿌리시오
		// 실습 => 레파지토리를 제거 후 서비스로 교체
		List<Question> all = this.questionRepository.findAll();
		System.out.println( all.size() );
		// 템플릿에 전달할 데이터 세팅 
		// => SSR 스타일(서버측에서 html+데이터=>동적html생성=>클라이언트 전달)
		model.addAttribute("questions", all);
		// 화면처리
		return "question_view";
	}
	// 변하는 값 id를 url에 전달하고 싶은 경우 => @PathVariable 사용
	// 모두 같은 이름으로 구성 => id
	@RequestMapping("/question/detail2/{id}")
	//@ResponseBody
	public String detail2(Model model, @PathVariable("id") Integer id) {
		return "question_detail";
	}
}
