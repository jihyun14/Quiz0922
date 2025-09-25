package com.memory.treasures.user;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memory.treasures.exception.SignupValidationException;

import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AppUserApiController {

	private final AppUserService appUserService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
		try {
			appUserService.registerUser(signUpRequest);
			return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입 성공"));
		} catch(SignupValidationException e) {
			return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", "검증 실패", "errors", e.getErrors()));
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "fail", "message", "서버 오류가 발생"));
		}
		
	}
	
}
