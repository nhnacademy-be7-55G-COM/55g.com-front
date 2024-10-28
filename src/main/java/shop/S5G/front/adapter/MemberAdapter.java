package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.MemberRegistrationRequestDto;
import shop.S5G.front.dto.MessageDto;

//TODO 추후 유레카 등록 필요
@FeignClient(value = "shop-service")
public interface MemberAdapter {

    @PostMapping("/api/shop/member")
    ResponseEntity<MessageDto> registerMember(@RequestBody MemberRegistrationRequestDto memberRegistrationRequestDto);
}
