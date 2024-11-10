package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.coupon.CouponTemplateInquiryResponseDto;
import shop.s5g.front.dto.coupon.CouponTemplateRegisterRequestDto;

@FeignClient(name = "couponTemplate", url = "${gateway.url}")
public interface CouponTemplateAdapter {

    @PostMapping("/api/shop/admin/coupons/template")
    ResponseEntity<MessageDto> createCouponTemplate(@RequestBody CouponTemplateRegisterRequestDto couponTemplateRegisterRequestDto);

    @GetMapping("/api/shop/admin/coupons/templates")
    ResponseEntity<List<CouponTemplateInquiryResponseDto>> findCouponTemplates(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );
}
