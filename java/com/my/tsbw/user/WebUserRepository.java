package com.my.tsbw.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.tsbw.answer.Answer;

// JpaRepository를 상속받으면서 특정 엔티티의 레퍼지토리가 된다
// <특정엔티티, PK의타입>
public interface WebUserRepository extends JpaRepository<WebUser, Long> {
	// 사용자 이름으로 회원을 찾는 함수
	Optional<WebUser> findByName(String name);
}
