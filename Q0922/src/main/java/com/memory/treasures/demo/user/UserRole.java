package com.memory.treasures.demo.user;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole implements GrantedAuthority{

	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");
	
	private final String authority; 
}
