package com.okta.example.oauth2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class OAuth2DemoApplication_Okta_2_1 {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2DemoApplication_Okta_2_1.class, args);
	}

    @GetMapping("/")
    String home(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getFullName();
    }
}
