package com.my.tsbw.answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.tsbw.DataNotFoundException;
import com.my.tsbw.question.Question;
import com.my.tsbw.question.QuestionRepository;
import com.my.tsbw.user.WebUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	// 1. 답변 목록 가져오기 -> 답변은 질문에 종속적, 이 메소드는 의미가 없다
	public List<Answer> getList(){
		return this.answerRepository.findAll();
	}	
	
	// 3. 질문, 내용을 전달받으면, Answer 테이블에 입력
	public void create(Question question, String content, WebUser author){
		Answer a = new Answer();		
		a.setQuestion(question);
		a.setContent(content);
		a.setCreateDate(LocalDateTime.now());
		a.setAuthor(author);
		// 등록
		this.answerRepository.save(a);
	}
	
	// TODO 답변 획득
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }
    // TODO 수정
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }
    // TODO 삭제
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }
    // TODO 추천
    public void vote(Answer answer, WebUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }
}










