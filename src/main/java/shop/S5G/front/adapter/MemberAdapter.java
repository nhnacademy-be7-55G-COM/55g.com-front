package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shop-service")
public class MemberAdapter {

}
