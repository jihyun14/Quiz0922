package com.memory.treasures.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.memory.treasures.demo.user.AppUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity

public class SecurityConfig {

//	private final AuthenticationSuccessHandler successHandler;
//	private final AuthenticationFailureHandler failureHandler;
	private final AppUserDetailService appUserdetailService;
	
    private final PasswordEncoder passwordEncoder;

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

		return http
				.csrf((csrf) -> csrf.disable())
				.httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
	            .formLogin(formLogin -> formLogin.disable()) // Form Login 비활성화
			    // authenticationToken(Security가 만들어내는 토큰을 없애야, JWTToken이 활성화 가능하다.) 
				.sessionManagement(session -> session 
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용을 위해 STATELESS로 설정
				.logout(logout -> logout.disable())
				.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
						.requestMatchers("/", "/main", "/login", "/signup",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico",
                        "/h2-console/**", "/api/auth/login", "/api/auth/signup", "/api/auth/refresh").permitAll()
						.anyRequest().authenticated()
				)
						
				.headers(headers -> headers
				        .frameOptions(frame -> frame.sameOrigin())      // H2 콘솔 iframe 허용
				)
				.authenticationProvider(authenticationProvider())
				// API 요청이 들어올 때마다 JWT 토큰을 검증할 커스텀 필터 => JwtAuthenticationFilter
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	
	// DaoAuthenticationProvider 은 내부적으로 UserDetailsService를 사용하여 정보를 조회하고,
	// 입력된 비밀번호와 데이터베이스에 저장된 비밀번호를 비교
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(appUserdetailService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	
	
	
	// AuthenticationManager는 여러 AuthenticationProvider를 사용하여 인증을 시도하는데 주로 DaoAuthenticationProvider를 사용
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AppUserService appUserService) {
	    return new JwtAuthenticationFilter(jwtTokenProvider, appUserService);
	}

	
	
	
}
