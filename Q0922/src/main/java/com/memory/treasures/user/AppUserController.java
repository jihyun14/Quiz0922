package com.memory.treasures.user;

import org.springframework.stereotype.Controller;
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
	
	@GetMapping("/index")
	public String index() {
		return "index";
	}
}
