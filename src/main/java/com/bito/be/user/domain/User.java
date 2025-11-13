package com.bito.be.user.domain;

import com.bito.be.user.dto.UserCreateRequest;
import com.bito.be.user.enums.OauthPlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final Long id;
    private final String email;
    private final String nickName;
    private final OauthPlatformType platform;
    private final Long providerId;
    private final LocalDateTime connectedDt;

    public static User from(UserCreateRequest userCreateRequest) {
        return User.builder()
                .connectedDt(userCreateRequest.getConnectedDt())
                .providerId(userCreateRequest.getProviderId())
                .email(userCreateRequest.getEmail())
                .nickName(userCreateRequest.getNickName())
                .platform(userCreateRequest.getPlatform())
                .build();
    }
}
