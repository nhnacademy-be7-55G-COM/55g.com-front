package shop.s5g.front.service.order;

import shop.s5g.front.dto.order.OrderDetailInfoDto;

public interface OrderDetailService {

    OrderDetailInfoDto getOrderDetailAllInfos(long orderId);
}
