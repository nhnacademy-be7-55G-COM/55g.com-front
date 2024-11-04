package shop.S5G.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.S5G.front.dto.order.OrderCreateRequestDto;
import shop.S5G.front.dto.order.OrderCreateResponseDto;
import shop.S5G.front.dto.order.OrderDetailInfoDto;
import shop.S5G.front.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.front.dto.order.OrderQueryRequestDto;
import shop.S5G.front.dto.order.OrderWithDetailResponseDto;

@FeignClient(value = "order", url = "${gateway.url}", path = "/api/shop/orders")
public interface OrderAdapter {
    @GetMapping
    List<OrderWithDetailResponseDto> fetchOrderLists(@RequestParam long customerId);

    @GetMapping("/{orderId}")
    List<OrderDetailWithBookResponseDto> fetchOrderDetailsWithOrderId(@PathVariable("orderId") long orderId);

    @PostMapping
    ResponseEntity<OrderCreateResponseDto> createNewOrder(@RequestBody OrderCreateRequestDto createRequest);

    @GetMapping
    List<OrderWithDetailResponseDto> fetchOrderListsBetweenDates(@RequestParam OrderQueryRequestDto queryRequest);

    @GetMapping("/{orderId}?scope=all")
    OrderDetailInfoDto fetchOrderDetailInfo(@PathVariable long orderId);
}
