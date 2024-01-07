package com.alpha.www.client.service;

import com.alpha.www.client.dto.UserDto;
import com.alpha.www.client.entity.User;
import com.alpha.www.client.entity.VerificationToken;

public interface UserService {

	User registerUser(UserDto userDto);

	void saveVerificationTokenForUser(User user, String token);

	String validateVerificationToken(String token);

	VerificationToken generateNewVerificationToken(String oldToken);
}
