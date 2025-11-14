package com.bito.be.inquiry.dto;

import com.bito.be.inquiry.domain.Inquiry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "문의 신청 응답 DTO")
public class InquiryResponse {
    
    @Schema(description = "문의 ID", example = "1")
    private Long id;
    
    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;
    
    @Schema(description = "문의 내용", example = "프로젝트 관련 문의드립니다...")
    private String content;
    
    @Schema(description = "생성 일시")
    private LocalDateTime createDt;
    
    public static InquiryResponse from(Inquiry inquiry) {
        return InquiryResponse.builder()
                .id(inquiry.getId())
                .email(inquiry.getEmail())
                .content(inquiry.getContent())
                .createDt(inquiry.getCreateDt())
                .build();
    }
}
