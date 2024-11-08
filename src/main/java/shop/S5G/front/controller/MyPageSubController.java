package shop.S5G.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.S5G.front.dto.PageResponseDto;
import shop.S5G.front.dto.point.PointHistoryResponseDto;
import shop.S5G.front.service.point.PointerService;
import shop.S5G.front.utils.PageUtils;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageSubController {
    private final PointerService pointerService;

    @GetMapping("/point-history")
    public ModelAndView viewPointHistory(@PageableDefault(size=10) Pageable pageable) {
        PageResponseDto<PointHistoryResponseDto> list = pointerService.getHistory(pageable);
        ModelAndView mv = new ModelAndView("mypage/point-history");

        mv.addObject("historyList", list.content());
        PageUtils.pushPageInfoToModelAndView(mv, list);

        return mv;
    }
}
