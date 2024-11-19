package shop.s5g.front.domain.purchase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.ToString;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.utils.RandomStringProvider;

@ToString
public class OrderInformation implements Serializable {
    String randomOrderId;       // 토스페이먼츠에 보낼 RandomizeOrderId
    long orderId;               // Shop API에 주문 생성 요청에 대한 응답으로 가져오는 orderId PK
    long totalPrice;            // 결제할 금액
    long netPrice;              // 뭐였더라..

    OrderCreateRequestDto order;        // 주문
    public Map<Long, PurchaseCell> purchaseMap;    // 구매할 책에 대한 각종 정보를 담는 Map

    boolean ready = false;

    OrderInformation(RandomStringProvider randomStringProvider) {
        randomOrderId = randomStringProvider.nextString();
        purchaseMap = new HashMap<>();
    }
}
