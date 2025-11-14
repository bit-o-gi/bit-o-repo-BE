package com.bito.be.inquiry.repository;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.entity.InquiryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InquiryRepositoryImpl implements InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

    @Override
    public Inquiry save(Inquiry inquiry) {
        return inquiryJpaRepository.save(InquiryEntity.from(inquiry)).toDomain();
    }
}
