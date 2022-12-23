package com.my.tsbw.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 실습 1 : 해당 클레스를 컨트롤로 만들어라. /tsbw 라우팅 처리하여
// "홈페이지" 문고 출력
// 스토리보드 -> 서비스의 메인 줄기 나눠서 -> 업무별로 컨트롤러 생성
@Controller
public class MainController {
	// 요청 : http://localhost:8080/tsbw
	@RequestMapping("/tsbw")
	@ResponseBody
	public String tsbw() {
		System.out.print("hi");
		return "홈페이지";		
	}
	// 실습 : URL http://localhost:8080/ 라우팅 처리
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/")	
	public String home() {
		// 홈페이지 요청 -> 질문 목록 페이지( ~/question/todoList)
		// redirect:URL => 완전히 새로운 URL로 이동 => URL은 최종 도착한곳
		// forward:URL => 요청시 전달한 값들 들고 새로운 url로 이동, 주소 변경없음
		return "redirect:/question/todoList";
		//return "forward:/question/todoList";
	}
	
	/**
	 * TODO REST API
	 * @return
	 * - REST는 "Representational State Transfer"의 약어로
	 * - 하나의 URI는 하나의 고유한 리소스(Resource)를 대표하도록 설계된다는 개념
	 * - 디바이스의 종류에 상관없이 공통으로 데이터를 처리할 수 있도록 하는 방식을 REST
	 * - REST API는 사용자가 어떠한 요청을 했을 때 화면(HTML)을 리턴하지 않고,
	 * - Javascript를 이용해서 사용자가 필요로 하는 결과(데이터)만을 리턴해주는 방식
	 */
	@GetMapping("/qs")
    @ResponseBody // public @ResponseBody List<Map<String, Object>> findAllMember()와 같이 리턴 타입 앞에 선언 가능
    public List<Map<String, Object>> findAllMember() {
        List<Map<String, Object>> members = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Map<String, Object> member = new HashMap<>();
            member.put("id", i);
            member.put("name", i + "번 개발자");
            member.put("age", 10 + i);
            members.add(member);
        }
        return members;
    }
	
}
