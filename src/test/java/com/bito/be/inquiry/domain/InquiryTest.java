package com.bito.be.inquiry.domain;

import com.bito.be.inquiry.dto.InquiryCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Inquiry 도메인 테스트")
class InquiryTest {

    @Test
    @DisplayName("Builder를 사용한 Inquiry 생성")
    void createInquiry_WithBuilder() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        Inquiry inquiry = Inquiry.builder()
                .id(1L)
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .createDt(now)
                .updateDt(now)
                .build();

        // then
        assertThat(inquiry).isNotNull();
        assertThat(inquiry.getId()).isEqualTo(1L);
        assertThat(inquiry.getEmail()).isEqualTo("test@example.com");
        assertThat(inquiry.getContent()).isEqualTo("테스트 문의 내용입니다.");
        assertThat(inquiry.getCreateDt()).isEqualTo(now);
        assertThat(inquiry.getUpdateDt()).isEqualTo(now);
    }

    @Test
    @DisplayName("from() 정적 팩토리 메서드로 InquiryCreateRequest로부터 생성")
    void createInquiry_FromRequest() {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        // when
        Inquiry inquiry = Inquiry.from(request);

        // then
        assertThat(inquiry).isNotNull();
        assertThat(inquiry.getId()).isNull(); // 새로 생성된 객체는 ID가 없음
        assertThat(inquiry.getEmail()).isEqualTo("test@example.com");
        assertThat(inquiry.getContent()).isEqualTo("테스트 문의 내용입니다.");
        assertThat(inquiry.getCreateDt()).isNull(); // 저장 전이므로 시간 정보 없음
        assertThat(inquiry.getUpdateDt()).isNull();
    }

    @Test
    @DisplayName("from() 메서드가 요청의 모든 필드를 올바르게 매핑")
    void from_MapsAllFieldsCorrectly() {
        // given
        String email = "user@test.com";
        String content = "상세한 문의 내용입니다.";

        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email(email)
                .content(content)
                .build();

        // when
        Inquiry inquiry = Inquiry.from(request);

        // then
        assertThat(inquiry.getEmail()).isEqualTo(email);
        assertThat(inquiry.getContent()).isEqualTo(content);
    }
}
