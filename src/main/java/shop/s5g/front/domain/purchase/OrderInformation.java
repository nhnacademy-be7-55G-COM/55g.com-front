package shop.s5g.front.domain.purchase;

import java.util.HashMap;
import java.util.Map;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.utils.RandomStringProvider;

class OrderInformation {
    String randomOrderId;
    long orderId;
    long totalPrice;
    long netPrice;

    OrderCreateRequestDto order;
    Map<Long, PurchaseCell> purchaseMap;

    boolean ready = false;

    OrderInformation(RandomStringProvider randomStringProvider) {
        randomOrderId = randomStringProvider.nextString();
        purchaseMap = new HashMap<>();
    }
}
