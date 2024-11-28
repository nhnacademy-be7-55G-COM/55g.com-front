package shop.s5g.front.service.order;

import shop.s5g.front.dto.order.OrderDetailCancelRequestDto;
import shop.s5g.front.dto.order.OrderDetailInfoDto;

public interface OrderDetailService {

    OrderDetailInfoDto getOrderDetailAllInfos(String uuid);

    OrderDetailInfoDto getOrderDetailAllInfos(long uuid);

    void cancelDetailRequest(long detailId, OrderDetailCancelRequestDto cancelReason);

    void changeOrderDetailStatus(long detailId, String typeName);
}
