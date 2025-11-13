package com.bito.be.config.oauth;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.kakao")
public class KakaoOAuth2Properties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String responseType;
    private String scope;
    private String authorizationUri;
}
