package shop.S5G.front.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.S5G.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.S5G.front.service.wrappingpaper.WrappingPaperService;

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
        // 포장지 가져오는 로직.
        CompletableFuture<List<WrappingPaperResponseDto>> wrapsFuture = wrappingPaperService.fetchAllPapersAsync();
        // TODO: 적립정책 가져오는 로직.
        mv.addObject("wrappingPaperList", wrapsFuture.join());
        return mv;
    }
}
