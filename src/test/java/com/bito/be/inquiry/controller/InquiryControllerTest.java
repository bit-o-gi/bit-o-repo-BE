package com.bito.be.inquiry.controller;

import com.bito.be.inquiry.domain.Inquiry;
import com.bito.be.inquiry.dto.InquiryCreateRequest;
import com.bito.be.inquiry.service.InquiryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("InquiryController 테스트")
class InquiryControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private InquiryService inquiryService;

    @InjectMocks
    private InquiryController inquiryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inquiryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("문의 생성 성공")
    void createInquiry_Success() throws Exception {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .build();

        Inquiry mockInquiry = Inquiry.builder()
                .id(1L)
                .email("test@example.com")
                .content("테스트 문의 내용입니다.")
                .createDt(LocalDateTime.now())
                .updateDt(LocalDateTime.now())
                .build();

        given(inquiryService.createInquiry(any(InquiryCreateRequest.class)))
                .willReturn(mockInquiry);

        // when & then
        mockMvc.perform(post("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.content").value("테스트 문의 내용입니다."))
                .andExpect(jsonPath("$.createDt").exists());
    }

    @Test
    @DisplayName("문의 생성 실패 - 이메일 누락")
    void createInquiry_Fail_NoEmail() throws Exception {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .content("테스트 문의 내용입니다.")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문의 생성 실패 - 내용 누락")
    void createInquiry_Fail_NoContent() throws Exception {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("test@example.com")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("문의 생성 실패 - 잘못된 이메일 형식")
    void createInquiry_Fail_InvalidEmail() throws Exception {
        // given
        InquiryCreateRequest request = InquiryCreateRequest.builder()
                .email("invalid-email")
                .content("테스트 문의 내용입니다.")
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
