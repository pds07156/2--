package com.my.tsbw.answer;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.my.tsbw.question.Question;
import com.my.tsbw.question.QuestionForm;
import com.my.tsbw.question.QuestionService;
import com.my.tsbw.user.WebUser;
import com.my.tsbw.user.WebUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	private final AnswerService answerService;
	private final QuestionService questionService;
	private final WebUserService webUserService;
	
	// /answer/create/1 => 라우팅 준비
	@PostMapping(value="/create/{id}")
	@PreAuthorize("isAuthenticated()")
	// @PreAuthorize이 작동할려면 SecurityConfig 에서 작동되게 등록
	public String create(
			Model model, 
			@PathVariable("id") Integer id, 
			// @RequestParam String content
			// 유효성 검사 기능추가
			@Valid AnswerForm answerForm, 
			BindingResult bindingResult,
			Principal principal // 사용자 이름을 얻을수 있다
	) {
		Question q = questionService.getQuestionById(id);
		// 0. 입력폼 유효성 검사
		if( bindingResult.hasErrors() ) {
			model.addAttribute("question", q);
			return "question_detail";
			//return "redirect:/question/detail/" + id; //<- 오류 내용이 않보인다(새로페이지구성됨)
		}
		WebUser user = webUserService.getUser( principal.getName() );
		// 1. 답변 등록
		answerService.create( q , answerForm.getContent(), user);		
		// 2. 해당 질문 상세보기로 이동
		return "redirect:/question/detail/" + id;
		//return String.format("redirect:/question/detail/%s", id);		
	}
	// TODO : 답변 수정
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }
	// TODO : 답변 수정
	@PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
            @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
	// TODO : 답변 삭제
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
	// TODO : 추천
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id) {
	    Answer answer = this.answerService.getAnswer(id);
	    WebUser siteUser = this.webUserService.getUser(principal.getName());
	    this.answerService.vote(answer, siteUser);
	    return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
}







