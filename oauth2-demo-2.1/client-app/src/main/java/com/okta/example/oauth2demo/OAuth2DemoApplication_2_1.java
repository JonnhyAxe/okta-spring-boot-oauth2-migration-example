package com.okta.example.oauth2demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@SpringBootApplication
public class OAuth2DemoApplication_2_1 {

    @Value("#{ @environment['spring.security.oauth2.resource.server'] }")
    private String resourceServerUrl;

    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public OAuth2DemoApplication_2_1(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

	public static void main(String[] args) {
		SpringApplication.run(OAuth2DemoApplication_2_1.class, args);
	}

    @GetMapping("/")
    String home(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getFullName();
    }

    @GetMapping("/api")
    String time(@AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
            oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName()
        );
        return tokenRelayTemplate(client.getAccessToken().getTokenValue()).getForObject(resourceServerUrl + "/api", String.class);
    }

    private RestTemplate tokenRelayTemplate(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(
                    HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution
            ) throws IOException {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, bytes);
            }
        };
    }
}
