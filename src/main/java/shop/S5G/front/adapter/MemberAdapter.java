package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.jwt.TokenResponseDto;
import shop.S5G.front.dto.member.MemberInfoResponseDto;
import shop.S5G.front.dto.member.MemberLoginRequestDto;
import shop.S5G.front.dto.member.MemberRegistrationRequestDto;
import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.dto.member.MemberUpdateRequestDto;

@FeignClient(name = "member", url = "${gateway.url}")
public interface MemberAdapter {

    @PostMapping("/api/shop/member")
    ResponseEntity<MessageDto> registerMember(@RequestBody MemberRegistrationRequestDto memberRegistrationRequestDto);

    @GetMapping("/api/shop/member")
    ResponseEntity<MemberInfoResponseDto> getMemberInfo();

    @PutMapping("/api/shop/member")
    ResponseEntity<MessageDto> updateMember(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto);
}
