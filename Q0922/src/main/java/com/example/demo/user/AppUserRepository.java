package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

	Optional<AppUser> findByUsername(String username);
	
	boolean existsByUsername(String username);
	boolean existsByNickname(String nickname);
	
	@Modifying
	@Query(value = "UPDATE USERS u SET u.ORDERCOUNT = u.ORDERCOUNT + 1 WHERE u.ID = :userId", nativeQuery = true)
	void incremenntOrderCount(@Param("userId") Long userId);
}
