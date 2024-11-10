package shop.s5g.front.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PurchaseController {
    private final WrappingPaperService wrappingPaperService;

    @GetMapping("/purchase")
    public ModelAndView getPurchaseView(/* User Auth */) {
        ModelAndView mv = new ModelAndView("create-order");
        log.trace("Getting shopping cart...");
        // TODO: 장바구니 가져오는 로직.
        //  장바구니가 비어있으면 장바구니로 리디렉트되도록 함.
        // 포장지 가져오는 로직.
        CompletableFuture<List<WrappingPaperResponseDto>> wrapsFuture = wrappingPaperService.fetchActivePapersAsync();
        // TODO: 적립정책 가져오는 로직.
        mv.addObject("wrappingPaperList", wrapsFuture.join());
        // TODO: CustomerKey 바꿔야함.
        mv.addObject("customerKey", UUID.randomUUID().toString());
        return mv;
    }
}
