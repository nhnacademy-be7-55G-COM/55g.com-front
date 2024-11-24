package shop.s5g.front.service.point.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.point.PointPolicyAdapter;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.PointPolicyService;

@Service
@RequiredArgsConstructor
public class PointPolicyServiceImpl implements PointPolicyService {
    private final PointPolicyAdapter pointPolicyAdapter;
    private final MemberService memberService;

    @Override
    public PointPolicyView getPurchasePointPolicy() {
        return pointPolicyAdapter.getPurchasePolicy();
    }

    @Override
    @Async("purchaseExecutor")
    public CompletableFuture<PointPolicyView> getPurchasePointPolicyAsync() {
        return CompletableFuture.completedFuture(getPurchasePointPolicy());
    }

    @Override
    public List<PointPolicyResponseDto> getPolicies() {
        return pointPolicyAdapter.getPolicies();
    }

    @Override
    public BigDecimal getPointAccRateForPurchase() {
        MemberInfoResponseDto info = memberService.getMemberInfo();
        PointPolicyView policy = getPurchasePointPolicy();

        BigDecimal gradePointRate = BigDecimal.valueOf(info.grade().point(), 2);
        return policy.value().add(gradePointRate);
    }
}
