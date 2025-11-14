package com.bito.be.inquiry.entity;

import com.bito.be.base.BaseEntity;
import com.bito.be.inquiry.domain.Inquiry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiry")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InquiryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public static InquiryEntity from(Inquiry inquiry) {
        return InquiryEntity.builder()
                .id(inquiry.getId())
                .email(inquiry.getEmail())
                .content(inquiry.getContent())
                .build();
    }

    public Inquiry toDomain() {
        return Inquiry.builder()
                .id(id)
                .email(email)
                .content(content)
                .createDt(getCreateDt())
                .updateDt(getUpdateDt())
                .build();
    }
}
