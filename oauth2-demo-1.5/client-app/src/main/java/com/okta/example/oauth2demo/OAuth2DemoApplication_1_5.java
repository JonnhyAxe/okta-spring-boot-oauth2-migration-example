package com.okta.example.oauth2demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableOAuth2Sso
@RestController
@SpringBootApplication
public class OAuth2DemoApplication_1_5 {

    @Value("#{ @environment['security.oauth2.resource.server'] }")
    private String resourceServerUrl;

    private OAuth2ProtectedResourceDetails resource;

    public OAuth2DemoApplication_1_5(OAuth2ProtectedResourceDetails resource) {
        this.resource = resource;
    }

    public static void main(String[] args) {
        SpringApplication.run(OAuth2DemoApplication_1_5.class, args);
    }

    @GetMapping("/")
    String home(@AuthenticationPrincipal OAuth2Authentication authentication) {

        return "Hello " + authentication.getName();
    }

    @GetMapping("/api")
    String time(@AuthenticationPrincipal OAuth2Authentication authentication) {
        return tokenRelayTemplate(authentication).getForObject(resourceServerUrl + "/api", String.class);
    }

    private OAuth2RestTemplate tokenRelayTemplate(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        OAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken(details.getTokenValue()));

        return new OAuth2RestTemplate(resource, context);
    }
}
