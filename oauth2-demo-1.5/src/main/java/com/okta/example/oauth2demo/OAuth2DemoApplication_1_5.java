package com.okta.example.oauth2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableOAuth2Sso
@RestController
@SpringBootApplication
public class OAuth2DemoApplication_1_5 {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2DemoApplication_1_5.class, args);
	}

    @GetMapping("/")
    String home(@AuthenticationPrincipal Authentication authentication) {
        return "Hello " + authentication.getName();
    }
}
