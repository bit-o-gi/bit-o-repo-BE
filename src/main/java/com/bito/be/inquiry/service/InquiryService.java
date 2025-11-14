package com.bito.be.inquiry.service;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.dto.InquiryCreateRequest;

public interface InquiryService {
    Inquiry createInquiry(InquiryCreateRequest request);
}
