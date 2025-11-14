package com.bito.be.inquiry.domain;

import com.bito.be.inquiry.dto.InquiryCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Inquiry {
    private final Long id;
    private final String email;
    private final String content;
    private final LocalDateTime createDt;
    private final LocalDateTime updateDt;

    public static Inquiry from(InquiryCreateRequest request) {
        return Inquiry.builder()
                .email(request.getEmail())
                .content(request.getContent())
                .build();
    }
}
