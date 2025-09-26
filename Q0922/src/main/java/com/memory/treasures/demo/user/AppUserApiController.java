package com.memory.treasures.demo.user;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memory.treasures.demo.security.TokenResponse;

import groovy.util.logging.Slf4j;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AppUserApiController {

	private final AppUserService appUserService;
	private static final Logger logger = LoggerFactory.getLogger(AppUserApiController.class);
	 
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
		    
		logger.info("SignupRequest 들어옴: {}", signUpRequest);
			this.appUserService.registerUser(signUpRequest);
			return ResponseEntity.ok(Map.of("status", "success", "message", "회원가입 성공"));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest,  HttpServletResponse response){
		TokenResponse tokens = this.appUserService.loginUser(loginRequest);
		// 3. Access Token을 HttpOnly 쿠키에 담아 응답에 추가
        sendAccessTokenAsCookie(response, tokens.getAccessToken());
		return ResponseEntity.ok(Map.of("status", "success", "message", "로그인 성공", "tokens" , tokens));
	}
	
	private void sendAccessTokenAsCookie(HttpServletResponse response, String accessToken) {
		 // 1. HttpOnly Cookie 생성
	    Cookie cookie = new Cookie("accessToken", accessToken);
	    
	    // 2. HTTP 요청 시 자동으로 포함되도록 경로 설정
	    cookie.setPath("/"); 
	    
	    // 3. JS에서 접근 불가능하게 설정 (XSS 방어)
	    cookie.setHttpOnly(true); 
	    
	    // 4. HTTPS 환경에서만 전송되도록 설정 (권장, 배포 환경 필수)
	    // cookie.setSecure(true); 
	    
	    // 5. 쿠키 만료 시간 설정 (토큰 만료 시간과 동일하게)
	    cookie.setMaxAge( (int) (60 * 60 * 1000L)); 

	    // 6. 응답에 쿠키 추가
	    response.addCookie(cookie);
		
	}

	// 새로운 AccessToken 발급하기 위함 
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> body){
		String refrehToken = body.get("refreshToken");
		return ResponseEntity.ok(this.appUserService.newAccessToken(refrehToken));
	}
	
	@GetMapping("/profile")
	public ResponseEntity<AppUser> getUserProfile(Authentication authentication){
		String username = authentication.getName();
		AppUser userProfile = appUserService.getUserProfile(username);
        return ResponseEntity.ok(userProfile);
	}
	
	// AuthController.java 또는 별도 LogoutController
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletResponse response) {
	    // access_token 쿠키를 만료시켜 제거
	    Cookie cookie = new Cookie("accessToken", null);
	    cookie.setMaxAge(0); // 만료 시간을 0으로 설정
	    cookie.setPath("/");
	    cookie.setHttpOnly(true); 
	    // cookie.setSecure(true); // HTTPS 환경 시
	    response.addCookie(cookie);
	    return ResponseEntity.ok("로그아웃 성공");
	}
	
	
}
