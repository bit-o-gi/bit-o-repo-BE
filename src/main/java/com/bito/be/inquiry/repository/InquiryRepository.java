package com.bito.be.inquiry.repository;

import com.bito.be.inquiry.domain.Inquiry;

public interface InquiryRepository {
    Inquiry save(Inquiry inquiry);
}
