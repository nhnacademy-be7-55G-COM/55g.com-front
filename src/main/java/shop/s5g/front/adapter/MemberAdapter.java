package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;

@FeignClient(name = "member", url = "${gateway.url}")
public interface MemberAdapter {

    @PostMapping("/api/shop/member")
    ResponseEntity<MessageDto> registerMember(@RequestBody MemberRegistrationRequestDto memberRegistrationRequestDto);

    @GetMapping("/api/shop/member")
    ResponseEntity<MemberInfoResponseDto> getMemberInfo();

    @PutMapping("/api/shop/member")
    ResponseEntity<MessageDto> updateMember(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto);
}
