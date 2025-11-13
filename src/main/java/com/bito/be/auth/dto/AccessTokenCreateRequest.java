package com.bito.be.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenCreateRequest {
    private String refreshToken;
}
