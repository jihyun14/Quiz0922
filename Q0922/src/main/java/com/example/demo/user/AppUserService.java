package com.example.demo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.LoginFailedException;
import com.example.demo.exception.NotAuthenticatedException;
import com.example.demo.exception.SignupValidationException;
import com.example.demo.exception.TokenValidationException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.TokenResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppUserService {
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	
	
	public Map<String, String> validateSignup(SignupRequest signUpRequest) {
	    Map<String, String> errors = new HashMap<>();
	    if(this.appUserRepository.existsByUsername(signUpRequest.getUsername())) {
	        errors.put("username", "이미 사용 중인 사용자명입니다.");
	    }
	    if(this.appUserRepository.existsByNickname(signUpRequest.getNickname())) {
	        errors.put("nickname", "이미 사용 중인 닉네임입니다.");
	    }
	    if(!signUpRequest.isPasswordMatching()) {
	        errors.put("password", "입력된 두 비밀번호가 다릅니다.");
	    }
	    if(!signUpRequest.isTermsAgreed()) {
	        errors.put("termsAgreed", "약관을 확인해주세요!!");
	    }

	    return errors;
	}

	
	public void registerUser(SignupRequest signUpRequest) {
		Map<String, String> errors = validateSignup(signUpRequest);
		 if (!errors.isEmpty()) {
		        throw new SignupValidationException(errors);
		    }
		 
		if(signUpRequest.getUsername().equals("admin")) {
			AppUser user = new AppUser(signUpRequest.getUsername(), signUpRequest.getNickname(), passwordEncoder.encode(signUpRequest.getPassword()),
							UserRole.ADMIN, signUpRequest.getCharacterType(), signUpRequest.isTermsAgreed());
			this.appUserRepository.save(user);
		}
		else {
			AppUser user = new AppUser(signUpRequest.getUsername(), signUpRequest.getNickname(), passwordEncoder.encode(signUpRequest.getPassword()),
					UserRole.USER, signUpRequest.getCharacterType(), signUpRequest.isTermsAgreed());
			this.appUserRepository.save(user);
		}
		 
	}


	public TokenResponse loginUser(LoginRequest loginRequest) {
		AppUser user = this.appUserRepository.findByUsername(loginRequest.getUsername())
						.orElseThrow(() -> new LoginFailedException("사용자를 찾을 수 없습니다."));
		if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
		}
		
		Authentication authentication  = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), loginRequest.getPassword()
				)
			);
		
		int refreshDays = loginRequest.isRemeberMe() ? 30 : 7;
		String accessToken = this.jwtTokenProvider.generateAccessToken(authentication);
		String refreshToken = this.jwtTokenProvider.generateRefreshToken(authentication, refreshDays);
		
	
		return new TokenResponse(accessToken, refreshToken);
	}
	
	
	public TokenResponse newAccessToken(String refreshToken) {
		if(!this.jwtTokenProvider.validateToken(refreshToken)) {
			throw new TokenValidationException("Refresh Token이 유효하지 않습니다.");
		}
		
		Authentication authentication = this.jwtTokenProvider.getAuthentication(refreshToken);
		
		AppUser user = this.appUserRepository.findByUsername(authentication.getName())
					.orElseThrow(() -> new LoginFailedException("사용자를 찾을 수 없습니다."));
		
		// 새로운 토큰 생성 
		String newAccessToken = this.jwtTokenProvider.generateAccessToken(authentication);
		return new TokenResponse(newAccessToken, refreshToken);
	}


	public AppUser getUserProfile(String username) {
		return this.appUserRepository.findByUsername(username).get();

	}
}

	