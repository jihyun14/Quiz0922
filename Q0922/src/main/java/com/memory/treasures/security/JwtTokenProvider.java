package com.memory.treasures.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

//JWT 토큰 생성, 검증 , 파싱 등 JWT 관련 모든 작업 처리 
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	 private static final String SECRET_KEY = "4261656C64756E67";

	
	private final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000L; // 1시간
    private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L; // 7일

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


	// Access Token 생성
    public String generateAccessToken(Authentication authentication) {
    	String authorities = authentication.getAuthorities().
    						stream().map(GrantedAuthority::getAuthority)
    						.collect(Collectors.joining(","));
   
    	long now = (new Date()).getTime();
    	Date validity = new Date(now + ACCESS_TOKEN_VALIDITY);
    	
    	return Jwts.builder()
    			.subject(authentication.getName())
    			.claim("auth", authorities)
    			.issuedAt(new Date())
    			.expiration(validity)
    			.signWith(this.getSigningKey())
    			.compact();
    }
	
    // Refresh Token 생성 (별도 정보 없이 만료 시간만 길게)
    public String generateRefreshToken(Authentication authentication) {
    	long now = (new Date()).getTime();
    	Date validity = new Date(now + REFRESH_TOKEN_VALIDITY);
    	
    	return Jwts.builder()
    			.subject(authentication.getName())
    			.issuedAt(new Date())
    			.expiration(validity)
    			.signWith(this.getSigningKey())
    			.compact();
    }
    
    
    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
    	// Claims는 토큰에 포함된 사용자 정보와 메타 데이터를 포함 
    	Claims claims = Jwts.parser()
    						.verifyWith(this.getSigningKey())
    						.build()
    						.parseSignedClaims(token)
    						.getPayload();
    	
    	if(claims.getSubject() == null) {
    		throw new AuthenticationCredentialsNotFoundException("JWT Claims 비어있음");
    	}
    	
    	Collection<? extends GrantedAuthority> authorities = 
    			Arrays.stream(claims.get("auth").toString().split(","))
    					.map(SimpleGrantedAuthority::new)
    					.collect(Collectors.toList());
    	
    	UserDetails principal = new User(claims.getSubject(),"", authorities);
    	return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    
    // 토큰 정보를 검증하는 메서드 
    public boolean validateToken(String token) {
    	try {
    		Jwts.parser()
    			.verifyWith(this.getSigningKey()).build().parseSignedClaims(token);
    		return true;
    	} catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
    		System.out.println("잘못된 JWT 시그니처에요");
    	}catch(ExpiredJwtException e) {
			System.out.println("만료된 토큰이니 재발급이 필요해요");
		} catch(UnsupportedJwtException e) {
			System.out.println("지원하지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			System.out.println("토큰 형식 틀렸어요");
		} catch(Exception e) {
			System.out.println("의문의 에러");
		}
    	return false;
    }

}
