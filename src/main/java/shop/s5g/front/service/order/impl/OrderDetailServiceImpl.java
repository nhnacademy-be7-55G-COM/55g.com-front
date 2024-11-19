package shop.s5g.front.service.order.impl;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.OrderAdapter;
import shop.s5g.front.adapter.PaymentsAdapter;
import shop.s5g.front.dto.order.OrderDetailCancelRequestDto;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderAdapter orderAdapter;
    private final PaymentsAdapter paymentsAdapter;

    @Override
    public OrderDetailInfoDto getOrderDetailAllInfos(long orderId) {
        return orderAdapter.fetchOrderDetailInfo(orderId);
    }

    @Override
    public void cancelDetailRequest(long detailId, OrderDetailCancelRequestDto cancelReason) {
        Map<String, Object> body = new HashMap<>();
        body.put("detailId", detailId);
        body.put("cancelInfo", cancelReason);

        paymentsAdapter.cancelPayment(body);
    }
}
