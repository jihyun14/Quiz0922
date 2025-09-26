package com.example.demo.simulator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemorySimulatorController {

	@GetMapping("/memory")
	public String memory() {
		return "memory-simulator";
	}
	
}
