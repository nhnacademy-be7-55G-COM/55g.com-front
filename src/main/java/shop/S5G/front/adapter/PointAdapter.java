package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.S5G.front.dto.PageResponseDto;
import shop.S5G.front.dto.point.PointHistoryResponseDto;

@FeignClient(name = "point", url = "${gateway.url}", path = "/api/shop/point")
public interface PointAdapter {
    @GetMapping("/history")
    PageResponseDto<PointHistoryResponseDto> fetchPointHistory(@RequestParam int size, @RequestParam int page);
}
