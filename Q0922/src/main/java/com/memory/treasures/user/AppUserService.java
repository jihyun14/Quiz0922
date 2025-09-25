package com.memory.treasures.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.memory.treasures.exception.SignupValidationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppUserService {
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	
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
		AppUser user = new AppUser(signUpRequest.getUsername(), signUpRequest.getNickname(), passwordEncoder.encode(signUpRequest.getPassword()),
									UserRole.USER, signUpRequest.getCharacterType(), signUpRequest.isTermsAgreed());
		this.appUserRepository.save(user);
	}
	
	

}
