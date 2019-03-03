package com.okta.example.oauth2demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@SpringBootApplication
public class OAuth2DemoApplication_Okta_2_1 {

    @Value("#{ @environment['okta.oauth2.audience'] }")
    private String resourceServerUrl;

    private WebClient webClient;

    public OAuth2DemoApplication_Okta_2_1(WebClient webClient) {
        this.webClient = webClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(OAuth2DemoApplication_Okta_2_1.class, args);
    }

    @GetMapping("/")
    String home(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getFullName();
    }

    @GetMapping("/api")
    String time(@AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {
        return this.webClient
            .get()
            .uri(this.resourceServerUrl + "/api")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @Configuration
    public static class WebClientConfig {
        @Bean
        WebClient webClient(
            ClientRegistrationRepository clientRegistrations, OAuth2AuthorizedClientRepository authorizedClients
        ) {
            ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
            oauth2.setDefaultOAuth2AuthorizedClient(true);
            oauth2.setDefaultClientRegistrationId("okta");
            return WebClient.builder()
                .apply(oauth2.oauth2Configuration())
                .build();
        }
    }
}