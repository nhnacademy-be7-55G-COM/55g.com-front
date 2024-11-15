package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;

@FeignClient(name = "address", url = "${gateway.url}", path = "/api/shop/point/policies", configuration = FeignGatewayAuthorizationConfig.class)
public interface PointPolicyAdapter {
    @GetMapping
    List<PointPolicyResponseDto> getPolicies();

    @GetMapping("/purchase")
    PointPolicyView getPurchasePolicy();
}
