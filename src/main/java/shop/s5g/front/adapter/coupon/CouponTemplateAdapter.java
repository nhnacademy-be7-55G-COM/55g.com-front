package shop.s5g.front.adapter.coupon;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

@FeignClient(name = "couponTemplate", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CouponTemplateAdapter {

    @PostMapping("/api/shop/admin/coupons/template")
    ResponseEntity<MessageDto> createCouponTemplate(@RequestBody CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);

    @GetMapping("/api/shop/admin/coupons/templates")
    ResponseEntity<List<CouponTemplateInquiryResponseDto>> findCouponTemplates(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/api/shop/admin/coupons/template/{templateId}")
    ResponseEntity<CouponTemplateInquiryResponseDto> findCouponTemplateById(@PathVariable("templateId") Long templateId);

    @GetMapping("/api/shop/admin/coupons/templates/unused")
    ResponseEntity<Page<CouponTemplateInquiryResponseDto>> findCouponTemplatesUnused(Pageable pageable);
}
