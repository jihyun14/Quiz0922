package com.example.demo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.user.AppUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
	private final AppUserService appUserService;
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
	// 공개 경로는 JWT 인증 스킵
	// 인증이 필요없는 경로들
    private static final String[] PERMIT_URL_ARRAY = {
    		"/", "/main", "/login", "/signup",
            "/css/**", "/js/**", "/images/**", "/favicon.ico",
            "/h2-console/**", "/api/auth/signup", "/api/auth/login", "/api/auth/refresh"
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
		// H2 콘솔 접근을 JWT 필터에서 제외
		if (requestURI.startsWith("/h2-console")) {
            filterChain.doFilter(request, response); 
            return;
        }
		
		String jwt = resolveToken(request);
	
		if(StringUtils.hasText(jwt)) {
			if(this.jwtTokenProvider.validateToken(jwt)) {
				Authentication auth = this.jwtTokenProvider.getAuthentication(jwt);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				// AccessToken 만료 -> RefreshToken 확인
				String refreshToken = request.getHeader("Refresh-Token");
				if(StringUtils.hasText(refreshToken) && this.jwtTokenProvider.validateToken(refreshToken)) {
					// RefreshToken으로 새 AccessToken 발급
					TokenResponse newTokens = this.appUserService.newAccessToken(refreshToken);
					
					Authentication auth = this.jwtTokenProvider.getAuthentication(newTokens.getAccessToken());
					SecurityContextHolder.getContext().setAuthentication(auth);
					
					// 새로 발급받은 토큰을 클라이언트에 전달
					response.setHeader("Authorization", "Bearer " +newTokens.getAccessToken());
					response.setHeader("Refresh-Token", newTokens.getRefreshToken());
					
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

	
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}
		
		 // 2. (추가 로직) HttpOnly 쿠키에서 토큰 확인
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            // 쿠키 이름이 'access_token' 인지 확인
	            if ("accessToken".equals(cookie.getName())) { 
	                return cookie.getValue(); // 쿠키 값(JWT) 반환
	            }
	        }
	    }
		return null;
	}


}
