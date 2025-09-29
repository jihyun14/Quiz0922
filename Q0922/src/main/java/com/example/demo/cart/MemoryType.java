package com.example.demo.cart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemoryType {

	HAPPY("행복", "😊"),
    SAD("슬픔", "😢"),
    DREAM("꿈", "💭"),
    MUNDANE("평범", "🗓️");
	
	private final String name;
	private final String icon;
	
}
