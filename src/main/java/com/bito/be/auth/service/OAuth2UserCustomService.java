package com.bito.be.auth.service;

import com.bito.be.user.domain.User;
import com.bito.be.user.enums.OauthPlatformType;
import com.bito.be.user.repository.UserRepository;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 요청을 바탕으로 유저 정보를 담은 객체 반환
        OAuth2User user = super.loadUser(userRequest);
        saveOrUpdate(user);
        return user;
    }

    // 유저가 있으면 업데이트, 없으면 유저 생성
    private void saveOrUpdate(OAuth2User user) {
        Map<String, Object> attribute = user.getAttributes();
        // Kakao 사용자 정보에서 nickname을 가져옴
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");
        String nickname = (properties != null && properties.containsKey("nickname")) ? (String) properties.get("nickname") : null;

        // Kakao 계정 정보에서 email을 가져옴
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        String email = (kakaoAccount != null && kakaoAccount.containsKey("email")) ? (String) kakaoAccount.get("email") : null;

        // 사용자 ID
        Long id = (Long) attribute.get("id");

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(
                (String) attribute.get("connected_at"), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime connectedAt = offsetDateTime.toLocalDateTime();

        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isEmpty()) {
            User userDomain = User.builder()
                    .nickName(nickname)
                    .providerId(id)
                    .email(email)
                    .platform(OauthPlatformType.KAKAO)
                    .connectedDt(connectedAt)
                    .build();

            userRepository.save(userDomain);
        }

    }
}
