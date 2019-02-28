package com.okta.example.oauth2demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Collections;

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
        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        OAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultOAuth2AccessToken(details.getTokenValue()));
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, context);

        oAuth2RestTemplate.setRequestFactory(factory);
        oAuth2RestTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

        return oAuth2RestTemplate;
    }
}
