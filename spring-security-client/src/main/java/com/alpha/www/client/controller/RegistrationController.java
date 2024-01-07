package com.alpha.www.client.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.www.client.dto.UserDto;
import com.alpha.www.client.entity.User;
import com.alpha.www.client.entity.VerificationToken;
import com.alpha.www.client.event.RegistrationCompleteEvent;
import com.alpha.www.client.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RegistrationController {

	@Autowired
	private UserService userService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@PostMapping("/register")
	public String registerUser(@RequestBody UserDto userDto, final HttpServletRequest request) {
		User user = userService.registerUser(userDto);
		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success";
	}
	
	@GetMapping("/verifyRegistration")
	public String verifyRegistration(@RequestParam("token") String token) {
		String result = userService.validateVerificationToken(token);
		if (result.equalsIgnoreCase("valid")) {
			return "User Verified Successfully";
		}
		return "Bad User";
	}
	
	@GetMapping("/resendVerifyToken")
	public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
		User user = verificationToken.getUser();
		resendVerificationTokenMail(user, applicationUrl(request), verificationToken);
		return "Verification Link Sent";
	}

	private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
		String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();
		
		// sendVerificationEmail()
		log.info("Click the link to verify your account: {}", url);
	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
