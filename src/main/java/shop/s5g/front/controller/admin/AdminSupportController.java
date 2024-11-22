package shop.s5g.front.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.front.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.front.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperCreateRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.delivery.DeliveryFeeService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/support")
@Slf4j
public class AdminSupportController {
    private final DeliveryFeeService deliveryFeeService;
    private final WrappingPaperService wrappingPaperService;

    @PutMapping("/delivery-fee")
    public DeliveryFeeResponseDto updateDeliveryFee(@RequestBody DeliveryFeeUpdateRequestDto updateRequest) {
        return deliveryFeeService.updateFee(updateRequest);
    }

    @PostMapping("/delivery-fee")
    public DeliveryFeeResponseDto creatDeliveryFee(@RequestBody DeliveryFeeCreateRequestDto createRequest) {
        return deliveryFeeService.createFee(createRequest);
    }

    @PostMapping(value = "/wrappingpaper", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WrappingPaperResponseDto createWrappingPaper(@Valid WrappingPaperCreateRequestDto createRequest) {
        log.trace("Paper create request received; name={}, price={}", createRequest.name(), createRequest.price());
        return wrappingPaperService.createNewWrappingPaper(createRequest);
    }

    @DeleteMapping("/wrappingpaper/{id}")
    public ResponseEntity<HttpStatus> deleteWrappingPaper(@PathVariable long id) {
        wrappingPaperService.deletePaper(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
