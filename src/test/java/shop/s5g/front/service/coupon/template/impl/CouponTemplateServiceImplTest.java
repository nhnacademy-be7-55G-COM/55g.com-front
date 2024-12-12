package shop.s5g.front.service.coupon.template.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.coupon.CouponTemplateAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateRegisterRequestDto;
import shop.s5g.front.dto.coupon.template.CouponTemplateUpdateRequestDto;
import shop.s5g.front.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.front.exception.coupon.CouponTemplateRegisterFailedException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponTemplateServiceImplTest {

    @Mock
    private CouponTemplateAdapter couponTemplateAdapter;

    @InjectMocks
    private CouponTemplateServiceImpl couponTemplateService;

    private CouponTemplateInquiryResponseDto templateDto;
    private MessageDto messageDto;
    private CouponTemplateRegisterRequestDto registerRequestDto;
    private CouponTemplateUpdateRequestDto updateRequestDto;

    @BeforeEach
    void setUp() {
        // 기존 DTO와 달리 이제 CouponTemplateInquiryResponseDto는 그대로 사용 가능
        templateDto = new CouponTemplateInquiryResponseDto(
            1L,
            BigDecimal.valueOf(1000),
            500L,
            2000L,
            30,
            "TestCoupon",
            "TestDescription"
        );

        messageDto = new MessageDto("Success");

        // 변경된 DTO: CouponTemplateRegisterRequestDto(Long couponPolicyId, String couponName, String couponDescription)
        // 예: couponPolicyId = 2L, couponName = "RegisterTestCoupon", couponDescription = "Description"
        registerRequestDto = new CouponTemplateRegisterRequestDto(2L, "RegisterTestCoupon", "Description");

        // 변경된 DTO: CouponTemplateUpdateRequestDto(Long couponTemplateId, String couponName, String couponDescription)
        // 예: couponTemplateId = 1L, couponName = "UpdateTestCoupon", couponDescription = "UpdatedDesc"
        updateRequestDto = new CouponTemplateUpdateRequestDto(1L, "UpdateTestCoupon", "UpdatedDesc");
    }

    @Test
    void createCouponTemplate_Success() {
        when(couponTemplateAdapter.createCouponTemplate(registerRequestDto)).thenReturn(ResponseEntity.ok(messageDto));

        MessageDto result = couponTemplateService.createCouponTemplate(registerRequestDto);
        assertNotNull(result);
        assertEquals("Success", result.message());
    }

    @Test
    void createCouponTemplate_Failed() {
        when(couponTemplateAdapter.createCouponTemplate(registerRequestDto))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.createCouponTemplate(registerRequestDto));
    }

    @Test
    void createCouponTemplate_HttpClientErrorException() {
        when(couponTemplateAdapter.createCouponTemplate(registerRequestDto))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.createCouponTemplate(registerRequestDto));
    }

    @Test
    void createCouponTemplate_HttpServerErrorException() {
        when(couponTemplateAdapter.createCouponTemplate(registerRequestDto))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error"));

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.createCouponTemplate(registerRequestDto));
    }

    @Test
    void getCouponTemplates_Success() {
        Page<CouponTemplateInquiryResponseDto> page = new PageImpl<>(List.of(templateDto));
        when(couponTemplateAdapter.findCouponTemplates(any())).thenReturn(ResponseEntity.ok(page));

        Page<CouponTemplateInquiryResponseDto> result = couponTemplateService.getCouponTemplates(PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getCouponTemplates_NotFound() {
        when(couponTemplateAdapter.findCouponTemplates(any()))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(CouponTemplateNotFoundException.class, () -> couponTemplateService.getCouponTemplates(PageRequest.of(0,10)));
    }

    @Test
    void getCouponTemplates_HttpClientErrorException() {
        when(couponTemplateAdapter.findCouponTemplates(any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.getCouponTemplates(PageRequest.of(0,10)));
    }

    @Test
    void getCouponTemplatesUnused_Success() {
        Page<CouponTemplateInquiryResponseDto> page = new PageImpl<>(List.of(templateDto));
        when(couponTemplateAdapter.findCouponTemplatesUnused(any())).thenReturn(ResponseEntity.ok(page));

        Page<CouponTemplateInquiryResponseDto> result = couponTemplateService.getCouponTemplatesUnused(PageRequest.of(0,10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getCouponTemplatesUnused_NotFound() {
        when(couponTemplateAdapter.findCouponTemplatesUnused(any()))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(CouponTemplateNotFoundException.class, () -> couponTemplateService.getCouponTemplatesUnused(PageRequest.of(0,10)));
    }

    @Test
    void getCouponTemplatesUnused_HttpServerErrorException() {
        when(couponTemplateAdapter.findCouponTemplatesUnused(any()))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.getCouponTemplatesUnused(PageRequest.of(0,10)));
    }

    @Test
    void findCouponTemplateById_Success() {
        when(couponTemplateAdapter.findCouponTemplateById(1L)).thenReturn(ResponseEntity.ok(templateDto));

        CouponTemplateInquiryResponseDto result = couponTemplateService.findCouponTemplateById(1L);
        assertNotNull(result);
        assertEquals("TestCoupon", result.couponName());
    }

    @Test
    void findCouponTemplateById_NotFound() {
        when(couponTemplateAdapter.findCouponTemplateById(1L))
            .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        assertThrows(CouponTemplateNotFoundException.class, () -> couponTemplateService.findCouponTemplateById(1L));
    }

    @Test
    void findCouponTemplateById_HttpClientErrorException() {
        when(couponTemplateAdapter.findCouponTemplateById(1L))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(CouponTemplateRegisterFailedException.class, () -> couponTemplateService.findCouponTemplateById(1L));
    }

    @Test
    void deleteCouponTemplate_Success() {
        when(couponTemplateAdapter.deleteCouponTemplate(1L)).thenReturn(ResponseEntity.ok(messageDto));

        MessageDto result = couponTemplateService.deleteCouponTemplate(1L);
        assertNotNull(result);
        assertEquals("Success", result.message());
    }

    @Test
    void deleteCouponTemplate_Failed() {
        when(couponTemplateAdapter.deleteCouponTemplate(1L))
            .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> couponTemplateService.deleteCouponTemplate(1L));
        assertTrue(ex.getMessage().contains("Failed"));
    }

    @Test
    void deleteCouponTemplate_HttpServerErrorException() {
        when(couponTemplateAdapter.deleteCouponTemplate(1L))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class, () -> couponTemplateService.deleteCouponTemplate(1L));
    }

    @Test
    void updateCouponTemplate_Success() {
        when(couponTemplateAdapter.updateCouponTemplate(1L, updateRequestDto)).thenReturn(ResponseEntity.ok(messageDto));

        MessageDto result = couponTemplateService.updateCouponTemplate(1L, updateRequestDto);
        assertNotNull(result);
        assertEquals("Success", result.message());
    }

    @Test
    void updateCouponTemplate_Failed() {
        when(couponTemplateAdapter.updateCouponTemplate(1L, updateRequestDto))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> couponTemplateService.updateCouponTemplate(1L, updateRequestDto));
        assertTrue(ex.getMessage().contains("Failed"));
    }

    @Test
    void updateCouponTemplate_HttpClientErrorException() {
        when(couponTemplateAdapter.updateCouponTemplate(1L, updateRequestDto))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(RuntimeException.class, () -> couponTemplateService.updateCouponTemplate(1L, updateRequestDto));
    }

}
