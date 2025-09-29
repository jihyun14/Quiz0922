package com.example.demo.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	@GetMapping("/admin")
	public String admin() {
		return "admin-dashboard";
	}
	
}
