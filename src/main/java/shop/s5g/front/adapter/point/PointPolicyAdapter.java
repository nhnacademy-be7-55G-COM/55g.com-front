package shop.s5g.front.adapter.point;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.front.dto.point.PointPolicyView;

@FeignClient(name = "address", url = "${gateway.url}", path = "/api/shop/point/policies", configuration = FeignGatewayAuthorizationConfig.class)
public interface PointPolicyAdapter {
    @GetMapping
    List<PointPolicyResponseDto> getPolicies();

    @GetMapping("/purchase")
    PointPolicyView getPurchasePolicy();

    @PostMapping("/update")
    ResponseEntity<Void> updatePolicy(
        @RequestBody PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto);
}
