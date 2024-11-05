package shop.S5G.front.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.OrderAdapter;
import shop.S5G.front.dto.order.OrderDetailInfoDto;
import shop.S5G.front.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderAdapter orderAdapter;

    @Override
    public OrderDetailInfoDto getOrderDetailAllInfos(long orderId) {
        return orderAdapter.fetchOrderDetailInfo(orderId);
    }
}
