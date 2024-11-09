package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.front.service.delivery.DeliveryFeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/support")
public class AdminSupportController {
    private final DeliveryFeeService deliveryFeeService;

    @PutMapping("/delivery-fee")
    public DeliveryFeeResponseDto updateDeliveryFee(@RequestBody DeliveryFeeUpdateRequestDto updateRequest) {
        return deliveryFeeService.updateFee(updateRequest);
    }

    @PostMapping("/delivery-fee")
    public DeliveryFeeResponseDto creatDeliveryFee(@RequestBody DeliveryFeeCreateRequestDto createRequest) {
        return deliveryFeeService.createFee(createRequest);
    }

}
