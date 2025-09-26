package com.memory.treasures.demo.security;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.memory.treasures.demo.user.AppUser;
import com.memory.treasures.demo.user.AppUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AppUserDetailService implements UserDetailsService{

	private final AppUserRepository appUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = this.appUserRepository.findByUsername(username)
							.orElseThrow(() -> new UsernameNotFoundException("사용자 찾을 수 없음" + username));
		user.setLastLoginAt(LocalDateTime.now());
		this.appUserRepository.save(user);
		
		Collection<GrantedAuthority> authorities = getAuthorities(user);
		return User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(authorities)
				.build();		
	}

	// 사용자 권한 목록 생성
	private Collection<GrantedAuthority> getAuthorities(AppUser user){
		List<GrantedAuthority> authorities = new ArrayList<>();
		if(user.getRole().equals(com.memory.treasures.demo.user.UserRole.ADMIN)) {
			authorities.add(new SimpleGrantedAuthority(com.memory.treasures.demo.user.UserRole.ADMIN.getAuthority()));
		} else {
			authorities.add(new SimpleGrantedAuthority(com.memory.treasures.demo.user.UserRole.USER.getAuthority()));
		}
		return authorities;
	} 
}



