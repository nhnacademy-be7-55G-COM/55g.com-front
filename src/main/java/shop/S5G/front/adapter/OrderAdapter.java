package shop.S5G.front.adapter;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.S5G.front.dto.order.OrderCreateRequestDto;
import shop.S5G.front.dto.order.OrderCreateResponseDto;
import shop.S5G.front.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.front.dto.order.OrderWithDetailResponseDto;

@FeignClient(value = "order", url = "${gateway.url}")
@RequestMapping("/api/shop/orders")
public interface OrderAdapter {
    @GetMapping
    List<OrderWithDetailResponseDto> fetchOrderLists(@RequestParam long customerId);

    @GetMapping("/{orderId}")
    List<OrderDetailWithBookResponseDto> fetchOrderDetailsWithOrderId(@PathVariable long orderId);

    @PostMapping
    ResponseEntity<OrderCreateResponseDto> createNewOrder(@RequestBody OrderCreateRequestDto createRequest);
}
