package com.project.uber.uberApp;

import com.project.uber.uberApp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail("harshsingh9425168491@gmail.com",
				"This is the testing email",
				"Body of my email");
	}

	@Test
	void setEmailMultiple(){
		String emails[] = {
				"ayushsingh1212.as@gmail.com",
				"harshsingh9425168491@gmail.com",
				"ranisingh94256@gmail.com",
                "hitendrapanse123@gmail.com"
		};
		emailSenderService.sendEmail(emails,"Hello from the UBER Apllication Demo",
				"Testing my first UBER Application");
	}

}
