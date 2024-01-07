package com.alpha.www.client.service.impl;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alpha.www.client.dto.UserDto;
import com.alpha.www.client.entity.User;
import com.alpha.www.client.entity.VerificationToken;
import com.alpha.www.client.repository.UserRepository;
import com.alpha.www.client.repository.VerificationTokenRepository;
import com.alpha.www.client.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(UserDto userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRole("USER");
		return userRepository.save(user);
	}

	@Override
	public void saveVerificationTokenForUser(User user, String token) {
		VerificationToken verificationToken = new VerificationToken(token, user);
		verificationTokenRepository.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {

		VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

		if (verificationToken == null) {
			return "invalid";
		}

		User user = verificationToken.getUser();
		Calendar calendar = Calendar.getInstance();

		if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
			verificationTokenRepository.delete(verificationToken);
			return "expired";
		}

		user.setEnabled(true);
		userRepository.save(user);

		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationTokenRepository.save(verificationToken);
		return verificationToken;
	}

}
