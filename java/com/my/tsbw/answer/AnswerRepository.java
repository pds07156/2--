package com.my.tsbw.answer;

import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository를 상속받으면서 특정 엔티티의 레퍼지토리가 된다
// <특정엔티티, PK의타입>
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
