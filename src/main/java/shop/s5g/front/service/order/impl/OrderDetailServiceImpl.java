package shop.s5g.front.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.OrderAdapter;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderAdapter orderAdapter;

    @Override
    public OrderDetailInfoDto getOrderDetailAllInfos(long orderId) {
        return orderAdapter.fetchOrderDetailInfo(orderId);
    }

//    @Override
//    public ResponseEntity<HttpStatus> cancelDetailRequest(long detailId) {
//        orderAdapter.
//    }
}
