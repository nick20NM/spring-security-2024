package com.alpha.www.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.www.client.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	VerificationToken findByToken(String token);

}
