package shop.s5g.front.adapter.point;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.point.PointPolicyFormResponseDto;
import shop.s5g.front.dto.point.PointSourceCreateRequestDto;

@FeignClient(name = "point-source", url = "${gateway.url}", path = "/api/shop/point/source", configuration = FeignGatewayAuthorizationConfig.class)
public interface PointSourceAdapter {

    @GetMapping("/create")
    ResponseEntity<List<PointPolicyFormResponseDto>> getPointPolicyFormData();

    @PostMapping("/create")
    ResponseEntity<Void> createPointSource(
        @RequestBody PointSourceCreateRequestDto pointSourceCreateRequestDto);
}
