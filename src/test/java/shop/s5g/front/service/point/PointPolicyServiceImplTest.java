package shop.s5g.front.service.point;

import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.point.PointPolicyAdapter;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.member.MemberGradeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberStatusResponseDto;
import shop.s5g.front.dto.point.PointPolicyResponseDto;
import shop.s5g.front.dto.point.PointPolicyView;
import shop.s5g.front.service.member.MemberService;
import shop.s5g.front.service.point.impl.PointPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PointPolicyServiceImplTest {

    @Mock
    PointPolicyAdapter pointPolicyAdapter;

    @Mock
    MemberService memberService;

    @InjectMocks
    PointPolicyServiceImpl service;

    @Test
    void getPurchasePointPolicyTest() {
        PointPolicyView pointPolicyView = new PointPolicyView("testName", BigDecimal.valueOf(100));

        when(pointPolicyAdapter.getPurchasePolicy()).thenReturn(pointPolicyView);

        PointPolicyView actualResult = service.getPurchasePointPolicy();

        Assertions.assertEquals(pointPolicyView, actualResult);
    }

    @Test
    void getPurchasePointPolicyAsyncTest() {
        PointPolicyView pointPolicyView = new PointPolicyView("testName", BigDecimal.valueOf(100));

        when(pointPolicyAdapter.getPurchasePolicy()).thenReturn(pointPolicyView);

        CompletableFuture<PointPolicyView> actualResult = service.getPurchasePointPolicyAsync();

        Assertions.assertEquals(pointPolicyView, actualResult.join());

    }

    @Test
    void getPoliciesTest() {
        List<PointPolicyResponseDto> expectedResult = new ArrayList<>();

        expectedResult.add(
            new PointPolicyResponseDto(1l, "policyName1", "sourceName1",
                BigDecimal.valueOf(1000l)));

        when(pointPolicyAdapter.getPolicies()).thenReturn(expectedResult);

        List<PointPolicyResponseDto> actualResult = service.getPolicies();

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getPointAccRateForPurchaseTest() {
        PointPolicyView policy = new PointPolicyView("testName", BigDecimal.valueOf(100));

        when(pointPolicyAdapter.getPurchasePolicy()).thenReturn(policy);

        MemberInfoResponseDto info = new MemberInfoResponseDto(1l,
            new MemberStatusResponseDto(1l, "active"),
            new MemberGradeResponseDto(1l, "gradeId1", 1, 1000), "loginId1", "password1",
            "2000-01-01", "name1", "email1", "010-1234-1234", LocalDateTime.now(),
            LocalDateTime.now(), 1000l, new ArrayList<AddressResponseDto>());

        when(memberService.getMemberInfo()).thenReturn(info);

        BigDecimal gradePointRate = BigDecimal.valueOf(info.grade().point(), 2);
        BigDecimal expectedResult = policy.value().add(gradePointRate);

        BigDecimal actualResult = service.getPointAccRateForPurchase();

        Assertions.assertEquals(expectedResult, actualResult);
    }


}
