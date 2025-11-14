package com.bito.be.inquiry.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "문의 신청 요청 DTO")
public class InquiryCreateRequest {
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "유효한 이메일 형식이어야 합니다")
    @Schema(description = "이메일 주소", example = "user@example.com")
    private String email;
    
    @NotBlank(message = "문의내용은 필수입니다")
    @Schema(description = "문의 내용", example = "프로젝트 관련 문의드립니다...")
    private String content;
}
