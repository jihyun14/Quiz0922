package com.example.demo.user;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

	@NotBlank(message = "이름은 필수입니다.")
	@Size(min = 4, max = 20, message = "사용자명은 4-20자 사이여야 합니다.")
	private String username;
	
	@NotBlank(message = "닉네임은 필수입니다")
	@Size(min =2, max = 10, message = "닉네임은 2-10자 사이여야 합니다.")
	private String nickname;
	
	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min =6, message = "비밀번호는 6자 이상이어야 합니다.")
	private String password;
	
	@NotBlank(message = "비밀번호 확인은 필수입니다.")
	private String confirmPassword;
	
	@AssertTrue(message = "약관을 확인해주세요.")
	private boolean termsAgreed;   // (약관 동의) 
	
	public boolean isTermsAgreed() {
		return termsAgreed;
	}
	
	@NotNull(message = "캐릭터를 선택해주세요.")
	private ChararcterType characterType;
	
	
	// 비밀번호 확인
	public boolean isPasswordMatching() {
		return password != null && password.equals(confirmPassword);
	}
}

