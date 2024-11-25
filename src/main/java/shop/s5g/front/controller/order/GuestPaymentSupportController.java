package shop.s5g.front.controller.order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.domain.purchase.GuestPurchaseSheet;
import shop.s5g.front.dto.CustomerInfoDto;
import shop.s5g.front.dto.customer.CustomerRegisterRequestDto;
import shop.s5g.front.dto.customer.CustomerResponseDto;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.PurchaseRequestDto;
import shop.s5g.front.dto.order.WrapModifyRequestDo;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.service.customer.CustomerService;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.utils.PaymentUtils;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/guest/payment/support")
@RedirectWithAlert(exceptions = UnsupportedOperationException.class, redirect = "/", title = "지원하지 않는 기능입니다.")
public class GuestPaymentSupportController {
    private final String tossPaymentsClientKey;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final GuestPurchaseSheet guestPurchaseSheet;

    @PostMapping("/customer")
    public CustomerInfoDto createOrGetCustomerInfo(
        @RequestParam String type,
        @RequestBody CustomerRegisterRequestDto register
    ) {
        return switch(type) {
            case "phoneNumber" -> {
                CustomerResponseDto response = customerService.createOrGetCustomerWithPN(
                    register.phoneNumber(), register.name(), register.password()
                );
                guestPurchaseSheet.setCustomer(response);
                yield new CustomerInfoDto(
                    String.valueOf(response.customerId()), PaymentUtils.getUUIDFromCustomerId(
                    response.customerId()), response.name(), response.email()
                );
            }
            case "email" -> throw new UnsupportedOperationException("미구현");
            default -> throw new IllegalArgumentException("지원하지 않는 인자입니다.");
        };
    }

    /**
     *  실행 순서:
     *     1. /customer 로 사용자 정보를 가져옴.
     *       1-0. 사용자가 쿠폰 등 여러가지 할인정책을 넣음.
     *       1-1. 사용자가 주문버튼을 누름.
     *     2. /toss/clientKey 로 토스 클라이언트 키를 가져옴.
     *     3. /create-order 로 주문을 생성함.
     *     4. /generate-order 로 토스 결제 ID를 생성함.
     *     5. 토스로 넘어감..
     */

    @GetMapping("/generate-order")
    public String generateOrderId(@RequestParam long orderId) {
        String value = guestPurchaseSheet.getRandomOrderId();
        guestPurchaseSheet.setOrderId(orderId);

        log.trace("New Order Id Generated: {}", value);
        return value;
    }

    @GetMapping("/customer")
    public CustomerInfoDto sendCustomerInfo() {
        CustomerResponseDto info = guestPurchaseSheet.getCustomerInfo();
        String customerIdStr = String.valueOf(info.customerId());
        String uuid = PaymentUtils.getUUIDFromCustomerId(info.customerId());

        return new CustomerInfoDto(
            customerIdStr, uuid, info.name(), info.email()
        );
    }

    @GetMapping("/toss/clientKey")
    public String getTossClientKey() {
        return tossPaymentsClientKey;
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderCreateResponseDto> createNewOrder(
        @Valid @RequestBody PurchaseRequestDto purchase,
        BindingResult errors
    ) {
        if (errors.hasErrors()) {
            throw new BadRequestException("주문 형식이 잘못되었습니다.");
        }
        OrderCreateRequestDto order = guestPurchaseSheet.createOrderRequest(purchase.delivery());

//        // TODO: 세션에서 쿠폰 사용 여부, 합계 및 netPrice 가져오기.

        return ResponseEntity.status(HttpStatus.CREATED).body(
            orderService.createGuestOrder(guestPurchaseSheet.getCustomerInfo().customerId(), order)
        );
    }

    @PutMapping("/wrap")
    public ResponseEntity<HttpStatus> modifyWraps(@RequestBody WrapModifyRequestDo wrapModify) {
        log.trace("Modifying wraps...: {}", wrapModify);
        guestPurchaseSheet.changeWrappingPaper(wrapModify);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/order")
    public ResponseEntity<HttpStatus> deleteOrder() {
        // 주문 세션에서 주문 ID를 가져와서 삭제 요청을 날림.
        orderService.deleteOrder(guestPurchaseSheet.getOrderId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
