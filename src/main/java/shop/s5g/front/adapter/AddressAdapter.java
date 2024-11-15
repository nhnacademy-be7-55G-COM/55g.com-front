package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;

@FeignClient(name = "address1", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface AddressAdapter {
    @GetMapping("/api/shop/address")
    ResponseEntity<List<AddressResponseDto>> getAddresses();

    @PostMapping("/api/shop/address")
    ResponseEntity<MessageDto> createAddress(@RequestBody AddressRequestDto address);

    @PutMapping("/api/shop/address/{addressId}")
    ResponseEntity<MessageDto> updateAddress(@PathVariable Long addressId, @RequestBody AddressUpdateRequestDto address);

    @DeleteMapping("/api/shop/address/{addressId}")
    ResponseEntity<MessageDto> deleteAddress(@PathVariable Long addressId);
}
