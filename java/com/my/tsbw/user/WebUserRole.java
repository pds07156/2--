package com.my.tsbw.user;

import lombok.Getter;

/**
 * 
 * @author Administrator
 * - 로그인한 사용자의 권한을 부여할때, 권한이 정의된 enum
 * - 권한은 여러개로 정의할수 있다
 */
@Getter
public enum WebUserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");	

	WebUserRole(String value) {
		this.value = value;
	}
	private String value;	
}
