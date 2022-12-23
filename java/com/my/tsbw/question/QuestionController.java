package com.my.tsbw.question;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.my.tsbw.answer.AnswerForm;
import com.my.tsbw.user.WebUser;
import com.my.tsbw.user.WebUserService;

import lombok.RequiredArgsConstructor;

// 이 컨트롤러에서 사용되는 URL prefix(접두사)를 공통으로 지정
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {
	private final QuestionService questionService;
	
	// TODO 질문 리스트
	//@RequestMapping("/question/todoList")
//	@RequestMapping("/todoList")
//	public String todoList(Model model) {
//		// 실습 => 레파지토리를 제거 후 서비스로 교체
//		List<Question> all = this.questionService.getList();
//		model.addAttribute("questions", all);
//		return "question_view";
//	}
	
	@RequestMapping("/todoList")
    public String list(Model model, 
    		@RequestParam(value="page", defaultValue="0") int page,
    		@RequestParam(value = "kw", defaultValue = "") String kw ) {
        Page<Question> paging = this.questionService.getList(page, kw);
        // 리스트 데이터 변경
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_view";
    }
	//@RequestMapping("/question/detail/{id}")
	// value=는 생략 가능
	@RequestMapping(value="/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id
			, AnswerForm answerForm) {
		// 최종 데이터를 Question(엔티티)로 받는 부분이 다소 보안성에 위배될수 있다
		// setXXX가 있어서 => DTO로 대체하라(권장)
		Question q = this.questionService.getQuestionById(id);
		model.addAttribute("question", q);
		return "question_detail";
	}

	// GET 방식 요청으로 처리한다면 @GetMapping으로 표현 (일반적으로 데이터를 보내지 않는 화면)
	@GetMapping("/create")
	// 이 메소드는 로그인이 필요한 메소드이다 => 로그인 한 이후에 사용 가능하다
	// 만약, 로그인을 않했는데 접근 => 로그인 페이지로 이동함
	@PreAuthorize("isAuthenticated()")
	public String create(QuestionForm questionForm) {
		return "question_form";
	}
	// rest api 스타일 중에 => url을 동일하게,  method로 구분해서 별도 처리 방식
	/*
	// POST 방식 요청에 대한 처리
	@PostMapping("/create2")
	// 기존 함수와 동일한 이름을 사용하되 파라미터를 다르게하여(오버로딩) 처리가 가능
	// 클라이언트가 요청할때 보낸 데이터를 인자로 받을수 있다
	// @ResponseBody	
	public String create2(@RequestParam String subject, @RequestParam String content) {
		// 1. 디비 저장 -> 서비스 -> 레피지토리 -> 엔티티 -> 디비
		this.questionService.create(subject, content);
		// 클라이언트 전송 데이터 확인
		//return subject + " : " + content;
		// 2. 질문 글 게시판으로 이동
		return "redirect:/question/todoList";
	}
	*/
	
	private final WebUserService webUserService;
	
	// @Valid : 폼클레스에 적용된 제약사항이 발동된다
	// subject, content 이런 키를 가진 데이터를 가진 폼(형태)이 전송되면
	// 스프링프레임웍이 QuestionForm 객체에 속성으로 자동 세팅(바인딩)해서 전달
	// BindingResult : @Valid -> 폼클레스에 적용된 유효사항 체크 결과가 전달
	// 항상 @Valid 다음 인자로 설정
	@PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public String create(@Valid QuestionForm questionForm, 
			BindingResult bindingResult,
			Principal principal // 사용자 이름을 얻을수 있다
			) { 
		if( bindingResult.hasErrors() ) { // 오류가 있다면
			return "question_form"; // "redirect:/question/create";
		}
		// 회원이 이름을 알면 => WebUserService.getUser => 3번인자로 삽입
		WebUser user = webUserService.getUser( principal.getName() );
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), user);		
		return "redirect:/question/todoList";
	}
	
	// 수정:GET=>기본 내용을 가져와서 등록폼에 뿌린채로 화면 표시
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, 
			@PathVariable("id") Integer id, Principal principal) {
		// 1. id -> 서비스 -> 질문 획득
		Question q = this.questionService.getQuestionById(id);
		// 2. 세션상의 유저명과 질문 작성가 동일하지 않으면 컷
		if( !principal.getName().equals( q.getAuthor().getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		// 3. 폼에 글 정보 등록	
		questionForm.setSubject( q.getSubject() );
		questionForm.setContent( q.getContent() );
		// 4. 응답 (SSR)
		return "question_form";
	}
	
	// 수정:POST => 실제 수정반영
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(
			@Valid QuestionForm questionForm, 
			BindingResult bindingResult, 
			Principal principal,
			@PathVariable("id") Integer id) {
		if( bindingResult.hasErrors() ) { // 오류가 있다면
			return "question_form";
		}
		Question q = this.questionService.getQuestionById(id);		
		if( !principal.getName().equals( q.getAuthor().getName() ) ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		// 수정
		questionService.modify(q, questionForm.getSubject(), questionForm.getContent());
		// 상세보기
		return "redirect:/question/detail/" + id;
		
	}
	
	
	// TODO : 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
	    Question question = this.questionService.getQuestionById(id);
	    if (!question.getAuthor().getName().equals(principal.getName())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
	    }
	    this.questionService.delete(question);
	    return "redirect:/";
	}
	
	
	
}







