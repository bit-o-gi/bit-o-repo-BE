package com.bito.be.inquiry.controller;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.dto.InquiryCreateRequest;
import com.bito.be.inquiry.dto.InquiryResponse;
import com.bito.be.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
@Tag(name = "Inquiry", description = "문의 신청 API")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<InquiryResponse> createInquiry(@Valid @RequestBody InquiryCreateRequest request) {
        Inquiry inquiry = inquiryService.createInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(InquiryResponse.from(inquiry));
    }
}
