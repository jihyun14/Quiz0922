package com.memory.treasures.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity

public class SecurityConfig {

//	private final AuthenticationSuccessHandler successHandler;
//	private final AuthenticationFailureHandler failureHandler;
	private final AppUserDetailService appUserdetailService;
	
	// private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

		return http
			
				.csrf((csrf) -> csrf.disable())
				.httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
	            .formLogin(formLogin -> formLogin.disable()) // Form Login 비활성화
				//.formLogin(form -> form
				//	    .loginPage("/login")        // 네가 만든 login.html 경로
				//	    .defaultSuccessUrl("/index") // 로그인 성공 후 이동
				//	    .permitAll()                // 누구나 접근 가능
				//	)
			    // authenticationToken(Security가 만들어내는 토큰을 없애야, JWTToken이 활성화 가능하다.) 
				.sessionManagement(session -> session 
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용을 위해 STATELESS로 설정
				.logout(logout -> logout.disable())
				.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers("/", "/main", "/login", "/signup",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico",
                        "/oauth2/**").permitAll())
				// .authenticationProvider(authenticationProvider())
				// API 요청이 들어올 때마다 JWT 토큰을 검증할 커스텀 필터 => JwtAuthenticationFilter
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(appUserdetailService);
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}
//	
//	
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	
	
}
