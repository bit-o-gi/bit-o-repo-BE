package com.bito.be.inquiry.service;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.dto.InquiryCreateRequest;
import com.bito.be.inquiry.repository.InquiryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;

    @Override
    public Inquiry createInquiry(InquiryCreateRequest request) {
        Inquiry inquiry = Inquiry.from(request);
        return inquiryRepository.save(inquiry);
    }
}
