package com.bito.be.user.entity;

import com.bito.be.base.BaseEntity;
import com.bito.be.user.domain.User;
import com.bito.be.user.enums.OauthPlatformType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oauth2_user")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String nickName;

    @Enumerated(EnumType.STRING)
    private OauthPlatformType platform;

    private Long providerId;

    private LocalDateTime connectedDt;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.nickName = user.getNickName();
        userEntity.platform = user.getPlatform();
        userEntity.providerId = user.getProviderId();
        userEntity.connectedDt = user.getConnectedDt();
        return userEntity;
    }

    public User toDomain() {
        return User.builder()
                .id(id)
                .email(email)
                .nickName(nickName)
                .platform(platform)
                .providerId(providerId)
                .connectedDt(connectedDt)
                .build();
    }
}
