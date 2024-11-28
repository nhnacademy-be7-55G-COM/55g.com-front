package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.order.DetailStatusChangeRequestDto;
import shop.s5g.front.dto.order.OrderAdminTableView;
import shop.s5g.front.dto.order.OrderCreateRequestDto;
import shop.s5g.front.dto.order.OrderCreateResponseDto;
import shop.s5g.front.dto.order.OrderDetailInfoDto;
import shop.s5g.front.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.front.dto.order.OrderQueryFilterDto;
import shop.s5g.front.dto.order.OrderWithDetailResponseDto;

// /api/shop/orders
@FeignClient(value = "order", url = "${gateway.url}", path = "/api/shop/orders", configuration = FeignGatewayAuthorizationConfig.class)
public interface OrderAdapter {
    @GetMapping
    List<OrderWithDetailResponseDto> fetchOrderLists(@RequestParam long customerId);

    @GetMapping("/{orderId}")
    List<OrderDetailWithBookResponseDto> fetchOrderDetailsWithOrderId(@PathVariable("orderId") long orderId);

    @PostMapping
    ResponseEntity<OrderCreateResponseDto> createNewOrder(@RequestBody OrderCreateRequestDto createRequest);

    @GetMapping
    List<OrderWithDetailResponseDto> fetchOrderListsBetweenDates(@RequestParam String startDate, @RequestParam String endDate);

    @GetMapping("/{orderId}?scope=all")
    OrderDetailInfoDto fetchOrderDetailInfo(@PathVariable long orderId);

    @DeleteMapping("/{orderId}")
    ResponseEntity<HttpStatus> deleteOrder(@PathVariable long orderId);

    @GetMapping("/admin")
    List<OrderAdminTableView> fetchOrdersForAdmin(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false) Integer page ,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String deliveryStatus
    );

    default List<OrderAdminTableView> fetchOrdersForAdmin(OrderQueryFilterDto filter) {
        return fetchOrdersForAdmin(filter.orderId(), filter.customerId(), filter.active(), filter.page(), filter.size(), filter.deliveryStatus());
    }

    @PostMapping("/guests/{customerId}")
    ResponseEntity<OrderCreateResponseDto> createNewGuestOrder(@PathVariable long customerId, @RequestBody OrderCreateRequestDto createRequest);

    @GetMapping("/guests")
    List<OrderWithDetailResponseDto> queryAllGuestOrders(
        @RequestParam String phoneNumber,
        @RequestParam String name,
        @RequestParam String password
    );

    @PutMapping("/details/{detailId}")
    ResponseEntity<HttpStatus> changeDetailType(
        @PathVariable long detailId,
        @RequestBody DetailStatusChangeRequestDto change
    );
}
