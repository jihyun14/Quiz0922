package com.example.demo.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChararcterType {
	TANGO("탱고", "모험가" , "🐱"),
	TOSIM("토심이","감성가","🐰"),
	WAFFLEBEAR("와플곰","과학자","🧸"),
	BUSYDOG("바쁘개", "업무맨","🐕‍🦺");

	private final String displayName;
	private final String description;
	private final String avatar;
	
	
}
