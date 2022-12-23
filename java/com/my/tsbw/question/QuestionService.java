package com.my.tsbw.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.tsbw.answer.Answer;
import com.my.tsbw.user.WebUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository questionRepository;
	// 1. 질문 목록 가져오기
	public List<Question> getList(){
		return this.questionRepository.findAll();
	}
	// TODO 페이징, 검색 
	public Page<Question> getList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate")); // or desc
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        //Pageable pageable = PageRequest.of(page, 10);
		Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
        //return this.questionRepository.findAllByKeyword(kw, pageable);
    }
	/*
	 * q  - Root, 즉 기준을 의미하는 Question 엔티티의 객체 (질문 제목과 내용을 검색하기 위해 필요)
	 * u1 - Question 엔티티와 SiteUser 엔티티를 아우터 조인(JoinType.LEFT)하여 만든 SiteUser 엔티티의 객체. 
	 *      Question 엔티티와 SiteUser 엔티티는 author 속성으로 연결되어 있기 때문에 q.join("author")와 
	 *      같이 조인해야 한다. (질문 작성자를 검색하기 위해 필요)
	 * a -  Question 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체. Question 엔티티와 
	 *      Answer 엔티티는 answerList 속성으로 연결되어 있기 때문에 q.join("answerList")와 같이 
	 *      조인해야 한다. (답변 내용을 검색하기 위해 필요)
	 * u2 - 바로 위에서 작성한 a 객체와 다시 한번 SiteUser 엔티티와 아우터 조인하여 만든 SiteUser 엔티티의 
	 *      객체 (답변 작성자를 검색하기 위해서 필요)
	 * 검색어(kw)가 포함되어 있는지를 like로 검색하기 위해 제목, 내용, 질문 작성자, 답변 내용, 답변 작성자
	 *      각각에 cb.like를 사용하고 최종적으로 cb.or로 OR 검색되게 하였다. 
	 */
	private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거 
                Join<Question, WebUser> u1 = q.join("author",     JoinType.LEFT);
                Join<Question, Answer>  a  = q.join("answerList", JoinType.LEFT);
                Join<Answer, WebUser>   u2 = a.join("author",     JoinType.LEFT);
                return cb.or(
                		cb.like(q.get("subject"), 	"%" + kw + "%"), 	// 제목 
                        cb.like(q.get("content"), 	"%" + kw + "%"),    // 내용 
                        cb.like(u1.get("name"), "%" + kw + "%"),    // 질문 작성자 
                        cb.like(a.get("content"), 	"%" + kw + "%"),    // 답변 내용 
                        cb.like(u2.get("name"), "%" + kw + "%")
                        );   // 답변 작성자 
            }
        };
    }
	
	// 2. ID넣어서 일치되는 질문 가져오기
	public Question getQuestionById(Integer id) {
		Optional<Question> q = this.questionRepository.findById(id);
		if( q.isPresent() ) {
			return q.get();
		}
		// 일치하는 질문이 없다
		// Exception을 만들어서 던져 보겟다
		// 사용자 정의 예외 클레스 => 프로젝트별로 별도로 생성 관리 가능
		throw new QuestionNoDataException("해당 ID로는 질문이 조회되지 않습니다.");
	}
	
	// 3. 제목, 내용을 전달받으면, Question 테이블에 입력
	public void create(String subject, String content, WebUser author){
		// 제목과 내용을 디비에 입력 => JUnit 테스트에 해당 코드가 있다 => 카피
		Question q = new Question(); // 1. 테이블에 1대1로 대응하는 엔티티객체생성
		q.setSubject(subject);
		q.setContent(content);
		q.setAuthor(author);
		q.setCreateDate(LocalDateTime.now());
		// 등록
		this.questionRepository.save(q);
	}
	
	// 4. 질문객체, 제목, 내용 => 수정
	public void modify(Question question, String subject, String content){
		question.setSubject(subject); // 제몫 수정
		question.setContent(content); // 내용 수정
		question.setModifyDate(LocalDateTime.now()); // 수전 시간
		this.questionRepository.save(question); 
	}
	
	// TODO : 삭제
	public void delete(Question question) {
	    this.questionRepository.delete(question);
	}
}










