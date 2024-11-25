package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.customer.CustomerRegisterRequestDto;
import shop.s5g.front.dto.customer.CustomerResponseDto;

@FeignClient(name = "customer", url = "${gateway.url}", path = "/api/shop/customer", configuration = FeignGatewayAuthorizationConfig.class)
public interface CustomerAdapter {
    String TYPE_PHONE_NUMBER = "phoneNumber";
    String TYPE_EMAIL = "email";

    @GetMapping
    CustomerResponseDto getCustomerInfo(
        @RequestParam String type,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String phoneNumber,
        @RequestParam String name,
        @RequestParam String password
    );

    @PostMapping
    CustomerResponseDto createOrReturnCustomerForPurchase(
        @RequestParam String type,
        @RequestBody CustomerRegisterRequestDto register
    );
}
