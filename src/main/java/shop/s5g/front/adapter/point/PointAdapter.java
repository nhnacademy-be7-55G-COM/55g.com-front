package shop.s5g.front.adapter.point;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.point.PointHistoryResponseDto;

@FeignClient(name = "point", url = "${gateway.url}", path = "/api/shop/point", configuration = FeignGatewayAuthorizationConfig.class)
public interface PointAdapter {
    @GetMapping("/history")
    PageResponseDto<PointHistoryResponseDto> fetchPointHistory(@RequestParam int size, @RequestParam int page);
}
