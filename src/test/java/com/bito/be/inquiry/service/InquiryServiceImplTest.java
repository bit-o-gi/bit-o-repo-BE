package com.bito.be.inquiry.service;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.dto.InquiryCreateRequest;
import com.bito.be.inquiry.repository.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("InquiryServiceImpl 테스트")
class InquiryServiceImplTest {

    @Mock
    private InquiryRepository inquiryRepository;

    @InjectMocks
    private InquiryServiceImpl inquiryService;

    @Test
    @DisplayName("문의 생성 성공")
    void createInquiry_Success() {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        Inquiry savedInquiry = Inquiry.builder()
                .id(1L)
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();

        given(inquiryRepository.save(any(Inquiry.class)))
                .willReturn(savedInquiry);

        // when
        Inquiry result = inquiryService.createInquiry(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getContent()).isEqualTo("테스트 문의 내용입니다.");
        assertThat(result.getCreateDt()).isNotNull();
        assertThat(result.getUpdateDt()).isNotNull();

        verify(inquiryRepository, times(1)).save(any(Inquiry.class));
    }

    @Test
    @DisplayName("문의 생성 시 도메인 객체가 정적 팩토리 메서드로 생성됨")
    void createInquiry_UsesDomainFactoryMethod() {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        Inquiry savedInquiry = Inquiry.builder()
                .id(1L)
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();

        given(inquiryRepository.save(any(Inquiry.class)))
                .willReturn(savedInquiry);

        // when
        Inquiry result = inquiryService.createInquiry(request);

        // then
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getContent()).isEqualTo(request.getContent());
    }
}
