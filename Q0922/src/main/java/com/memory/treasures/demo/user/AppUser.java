package com.memory.treasures.demo.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class AppUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false, unique= true)
	private String nickname;
	
	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChararcterType characterType;
	
	@Column(nullable = false)
	private boolean termsAgreed;   // (약관 동의) 
	
	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	// orderCount
	private Integer orderCount = 0;
	
	private LocalDateTime LastLoginAt = null;
	
	public void setLastLoginAt(LocalDateTime lastLoginAt) {
		LastLoginAt = lastLoginAt;
	}

	@Builder
	public AppUser(String username, String nickname, String password, UserRole role, ChararcterType characterType,
			boolean termsAgreed) {
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.role = role;
		this.characterType = characterType;
		this.termsAgreed = termsAgreed;
	}
	
	
}
