package com.alpha.www.client.event.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.alpha.www.client.entity.User;
import com.alpha.www.client.event.RegistrationCompleteEvent;
import com.alpha.www.client.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
	
	@Autowired
	private UserService userService;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {

		// 1) create the verification token for the user with link
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.saveVerificationTokenForUser(user, token);
		
		// 2) send mail to user
		String url = event.getApplicationUrl() 
				+ "/verifyRegistration?token=" 
				+ token;
		
		// sendVerificationEmail()
		log.info("Click the link to verify your account: {}", url);
	}

}
