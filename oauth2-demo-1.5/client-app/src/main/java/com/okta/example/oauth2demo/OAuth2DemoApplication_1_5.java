package com.okta.example.oauth2demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableOAuth2Sso
@RestController
@SpringBootApplication
public class OAuth2DemoApplication_1_5 {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2DemoApplication_1_5.class, args);
	}

    @GetMapping("/")
    Map<String, String> home(@AuthenticationPrincipal Authentication authentication) {

	    Map<String, String> ret = new HashMap<>();
	    ret.put("msg", "Hello " + authentication.getName());
	    ret.put("access_token", ((OAuth2AuthenticationDetails)((OAuth2Authentication)authentication).getDetails()).getTokenValue());

	    return ret;
    }
}
