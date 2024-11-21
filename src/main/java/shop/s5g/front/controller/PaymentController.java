package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.annotation.SessionRequired;
import shop.s5g.front.domain.purchase.PurchaseSheet;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.payment.TossPaymentInfo;
import shop.s5g.front.exception.order.OrderSessionNotAvailableException;
import shop.s5g.front.service.order.OrderService;
import shop.s5g.front.service.payments.PaymentsService;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
@Controller
public class PaymentController {
    private final PaymentsService paymentsService;
    private final OrderService orderService;

    // 프록시로 작동해서 ObjectProvider 를 사용하지 않아도 됨!
    private final PurchaseSheet purchaseSheet;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // https://docs.tosspayments.com/sdk/v2/js#%EC%9D%91%EB%8B%B5-21

    // TODO: 메모
    //  쿠폰사용여부, 장바구니 등을 세션에서 관리하고, javascript의 비동기 전달값과 비교하여 검증.

    @GetMapping("/success")
    @SessionRequired
    @RedirectWithAlert(exceptions = OrderSessionNotAvailableException.class, title = "주문 오류", redirect = "/purchase")
    public String requestSuccess(
        @RequestParam long amount,
        @RequestParam String orderId,
        @RequestParam String paymentKey,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        // TODO: 장바구니를 체크

        // TODO: 쿠폰사용여부를 체크
        //
        // TODO: 장바구니와 쿠폰사용여부를 체크하여 결제비용을 검증

        // TODO: 값이 다르면 결제를 cancel하는 api를 shop에 요청.

        long totalPrice = purchaseSheet.getTotalPrice();
        if (totalPrice != amount) {
            // TODO: 적절한 예외로 교체
            throw new IllegalArgumentException();
        }

        Map<String, Object> body = new HashMap<>();

        TossPaymentInfo tossPaymentInfo = new TossPaymentInfo(paymentKey, orderId, amount);
        body.put("payment", tossPaymentInfo);
        body.put("orderDataId", purchaseSheet.getOrderId());
        body.put("usedPoint", purchaseSheet.getUsedPoint());
//        body.put("orderDataId", session.getAttribute("orderDataId"));

        MessageDto message = paymentsService.confirmPayment(body);
        if (message == null) {
            log.trace("payment request was delayed.. pending view was sent");
            return "payments/pending";
        }
        log.trace("Toss Payment request success");

        HttpSession session = request.getSession(false);
        session.invalidate();
        LocalDate date = LocalDate.now();
        return String.format(
            "redirect:/mypage?startDate=%s&endDate=%s#orders",
            date.format(dateTimeFormatter), date.format(dateTimeFormatter)
        );
    }

    @GetMapping("/fail")
    public String requestFail(
        @RequestParam String code,
        @RequestParam String message,
        @RequestParam String orderId,
        Model model,
        HttpServletRequest request
    ) {
        final long orderDataId = purchaseSheet.getOrderId();
        log.warn("Payment failed!: [orderDataId={}, code={}, message={}, orderId={}", orderDataId, code, message, orderId);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        orderService.deleteOrder(orderDataId);
        model.addAttribute("title", "결제에 실패했어요.");
        model.addAttribute("message", message);
        model.addAttribute("redirect", "/");
        return "error/redirect";
    }
}
