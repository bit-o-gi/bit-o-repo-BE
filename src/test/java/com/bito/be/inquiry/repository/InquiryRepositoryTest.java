package com.bito.be.inquiry.repository;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.entity.InquiryEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(InquiryRepositoryImpl.class)
@DisplayName("InquiryRepository 테스트")
class InquiryRepositoryTest {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private InquiryJpaRepository inquiryJpaRepository;

    @Test
    @DisplayName("문의 저장 성공")
    void save_Success() {
        // given
        Inquiry inquiry = Inquiry.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        // when
        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        // then
        assertThat(savedInquiry).isNotNull();
        assertThat(savedInquiry.getId()).isNotNull();
        assertThat(savedInquiry.getEmail()).isEqualTo("test@example.com");
        assertThat(savedInquiry.getContent()).isEqualTo("테스트 문의 내용입니다.");
        assertThat(savedInquiry.getCreateDt()).isNotNull();
        assertThat(savedInquiry.getUpdateDt()).isNotNull();
    }

    @Test
    @DisplayName("Entity와 Domain 간 변환 테스트")
    void entityDomainConversion() {
        // given
        Inquiry originalInquiry = Inquiry.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        // when
        InquiryEntity entity = InquiryEntity.from(originalInquiry);
        InquiryEntity savedEntity = inquiryJpaRepository.save(entity);
        Inquiry convertedInquiry = savedEntity.toDomain();

        // then
        assertThat(convertedInquiry.getId()).isNotNull();
        assertThat(convertedInquiry.getEmail()).isEqualTo(originalInquiry.getEmail());
        assertThat(convertedInquiry.getContent()).isEqualTo(originalInquiry.getContent());
        assertThat(convertedInquiry.getCreateDt()).isNotNull();
        assertThat(convertedInquiry.getUpdateDt()).isNotNull();
    }

    @Test
    @DisplayName("저장 후 자동으로 생성/수정 시간이 설정됨")
    void save_AutoTimestamp() {
        // given
        Inquiry inquiry = Inquiry.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        // when
        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        // then
        assertThat(savedInquiry.getCreateDt()).isNotNull();
        assertThat(savedInquiry.getUpdateDt()).isNotNull();
        assertThat(savedInquiry.getCreateDt()).isEqualTo(savedInquiry.getUpdateDt());
    }
}
