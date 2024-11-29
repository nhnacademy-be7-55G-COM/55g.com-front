package shop.s5g.front.service.point;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.front.dto.point.PointPolicyView;

public interface PointPolicyService {

    PointPolicyView getPurchasePointPolicy();

    CompletableFuture<PointPolicyView> getPurchasePointPolicyAsync();

    List<PointPolicyResponseDto> getPolicies();

    BigDecimal getPointAccRateForPurchase();

    void updatePolicyValue(PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto);
}
