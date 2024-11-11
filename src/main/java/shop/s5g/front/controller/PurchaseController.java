package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PurchaseController {
    private final WrappingPaperService wrappingPaperService;
    private final MemberService memberService;

    @ModelAttribute("memberInfo")
    public MemberInfoResponseDto getMemberInfo() {
        return memberService.getMemberInfo();
    }

    /**
     * 여기서 주문세션이 시작됨.
     * /purchase에 접근하면 주문세션이 시작(최대 1시간).
     * 결제버튼을 누르면 세션의 값을 가져와서 실제 payment의 결제와 동일한지 체크해야함.
     * 결제가 완료되었거나 실패하면 세션이 종료됨.
     * 장바구니로만 주문한다면 이럴 필요는 없지만,
     * 장바구니와 무관한 단품주문의 경우에 장바구니랑 다를 수 있으므로 세션을 열었음.
     * 다른 좋은 방법이 있을텐데..
     */
    @GetMapping("/purchase")
    public ModelAndView getPurchaseView(/* User Auth */ HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("create-order");

        log.trace("Getting shopping cart...");
        // 주문 세션의 시작.
        HttpSession session = request.getSession();
        // 최대 1시간동안 열림.
        session.setMaxInactiveInterval(3600);

        // TODO: 장바구니 가져오는 로직.
        //  장바구니가 비어있으면 장바구니로 리디렉트되도록 함.
        // 포장지 가져오는 로직.
        CompletableFuture<List<WrappingPaperResponseDto>> wrapsFuture = wrappingPaperService.fetchActivePapersAsync();
        // TODO: 적립정책 가져오는 로직.
        mv.addObject("wrappingPaperList", wrapsFuture.join().stream().map(wrappingPaperService::convertToView));

        // 테스트 영역
        session.setAttribute("purchase-summation", 52600L);
        mv.addObject("summation", 52600L);
        mv.addObject("originPrice", 52600L);
        mv.addObject("discountPrice", 0L);
        mv.addObject("cartList", getTestCart());

        return mv;
    }

    record TestCartDto(
        long id,
        String title,
        long price,
        long discountPrice,
        int quantity,
        long totalPrice
    ) {}

    private List<TestCartDto> getTestCart() {
        return List.of(
            new TestCartDto(
                5, "트리 생쥐의 완벽한크리스마스 선물 대작전", 16800, 0, 2, 33600
            ),
            new TestCartDto(
                6, "안아주기 - 50편의 영화 대사가 들려주는 용기와 희망의 메시지", 19000, 0, 1, 19000
            )
        );
    }
}