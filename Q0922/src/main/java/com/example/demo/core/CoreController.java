package com.example.demo.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CoreController {

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/products")
	public String products() {
		return "products";
	}
	
}
