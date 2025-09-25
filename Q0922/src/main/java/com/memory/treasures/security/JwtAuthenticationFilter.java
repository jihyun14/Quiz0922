package com.memory.treasures.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//클라이언트에서 요청이 들어온 경우 
// 등록한 JwtAuthenticationFilter가 동작하여 해당 요청의 토큰 유효성을 검사
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtTokenProvider jwtTokenProvider;
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer ";
	
	 // 인증이 필요없는 경로들
    private static final String[] PERMIT_URL_ARRAY = {
    		"/**"
    };
    
    // 모든 HTTP 요청이 이 필터를 거쳐가게 될거임 .. 
    // 요청을 거쳐가면서 토큰을 추출해 제대로된 토큰인지 아닌지 검사....
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String requestURI = request.getRequestURI();
		for(String a : PERMIT_URL_ARRAY) {
			if(requestURI.equals(a)) {
				// permitAll 경로에서는 토큰 검증 생략 
				filterChain.doFilter(request, response);
				return;
			}
		}
		
		
		String jwt = resolveToken(request);
		
		if(StringUtils.hasText(jwt) &&jwtTokenProvider.validateToken(jwt)) {
			// 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
			Authentication auth = jwtTokenProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(auth);
			filterChain.doFilter(request, response);
		}

	}

	
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		return null;
	}
}
