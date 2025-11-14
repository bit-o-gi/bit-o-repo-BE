package com.bito.be.inquiry.repository;

import com.bito.be.inquiry.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<InquiryEntity, Long> {
}
