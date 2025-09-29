package com.example.demo.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class AppUserController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/signup")
	public String register() {
		return "signup";
	}
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/user-profile")
	public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
	    
	    // 1. 사용자 ID 획득
	    String username = userDetails.getUsername(); 
	    
	    // 2. HTML이 요구하는 모든 데이터 로드 및 Model에 추가 (이전 답변 참조)
	    // 예시: model.addAttribute("user", userService.findByUsername(username));
	    // 예시: model.addAttribute("userStats", userStatsService.getStats(username));
	    // ...
	    
	    return "user-profile"; 
	}
}
