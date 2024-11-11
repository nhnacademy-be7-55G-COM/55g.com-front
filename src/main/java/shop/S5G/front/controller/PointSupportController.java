package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.point.PointHistoryResponseDto;
import shop.s5g.front.service.point.PointHistoryService;

@RequestMapping("/point/support")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PointSupportController {
    private final PointHistoryService pointHistoryService;

    @GetMapping("/history")
    public PageResponseDto<PointHistoryResponseDto> fetchMyPointHistory(@PageableDefault(size=10)
    Pageable pageable) {
        log.trace("pageable: size={}, page={}", pageable.getPageSize(), pageable.getPageNumber());
        return pointHistoryService.getHistory(pageable);
    }
}
