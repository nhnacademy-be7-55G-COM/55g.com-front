package shop.s5g.front.adapter;

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
import shop.s5g.front.dto.member.IdCheckResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.dto.member.PasswordChangeRequestDto;

@FeignClient(name = "member", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface MemberAdapter {

    @PostMapping("/api/shop/member")
    ResponseEntity<MessageDto> registerMember(@RequestBody MemberRegistrationRequestDto memberRegistrationRequestDto);

    @GetMapping("/api/shop/member")
    ResponseEntity<MemberInfoResponseDto> getMemberInfo();

    @PutMapping("/api/shop/member")
    ResponseEntity<MessageDto> updateMember(@RequestBody MemberUpdateRequestDto memberUpdateRequestDto);

    @DeleteMapping("/api/shop/member")
    ResponseEntity<MessageDto> deleteMember();

    @PostMapping("/api/shop/checkId/{loginId}")
    ResponseEntity<IdCheckResponseDto> checkId(@PathVariable("loginId") String loginId);

    @PostMapping("/api/shop/member/password")
    ResponseEntity<MessageDto> changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequestDto);
}
