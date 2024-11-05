package shop.S5G.front.service.order;

import shop.S5G.front.dto.order.OrderDetailInfoDto;

public interface OrderDetailService {

    OrderDetailInfoDto getOrderDetailAllInfos(long orderId);
}
