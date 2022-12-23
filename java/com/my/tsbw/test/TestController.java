package com.my.tsbw.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 일반 클레스를 컨트롤러 형태로 구현 => 어노테이션
 * @author Administrator
 * 	- 컨트롤러 기능 부여
 * 		- @Controller 어노테이션을 클레스 선언부분 위에 두면, 이 클레스는 컨트롤러의 기능을 수행한다
 * 		- URL 매핑 핵심 기능
 *
 */
@Controller
public class TestController {
	// 이 함수에 /test 라는 url을 연결하겠다
	@RequestMapping("/test")
	// 응답 -> 문자열 자체로 응답하겟다
	@ResponseBody
	public String test() {
		return "Hello World7";
	}
}
