package com.my.tsbw.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// JpaRepository를 상속받으면서 특정 엔티티의 레퍼지토리가 된다
// <특정엔티티, PK의타입>
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	// JpaRepository에서 지원하지 않는 메소드(쿼리)가 있다면 커스텀으로 생성가능
	// 조회 => findBy + 컬럼명 => findBySubject, findBySubjectAndContent, findBySubjectLike
	// 메소드 이름을 분석해서 쿼리를 생성한다 (내부적으로는)
	
	// 특정 컬럼값만 체크 => 조회
	Question findBySubject(String subject);
	// 멀티컬럼 값을 체크 쿼리 => 조회 
	Question findBySubjectAndContent(String subject, String content);
	// like를 적용한 쿼리 => 조회
	List<Question> findBySubjectLike(String subject);
	
	// TODO 페이징
	Page<Question> findAll(Pageable pageable);
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("select "
            + "distinct q "
            + "from Question q " 
            + "left outer join WebUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join WebUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.name like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.name like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}
