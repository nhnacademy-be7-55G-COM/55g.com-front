package shop.S5G.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.front.dto.jwt.RefreshTokenRequestDto;
import shop.S5G.front.dto.jwt.TokenResponseDto;
import shop.S5G.front.dto.member.MemberLoginRequestDto;

@FeignClient(name = "auth-service", url = "${gateway.url}")
public interface AuthAdapter {
    @PostMapping("/api/auth/login")
    ResponseEntity<TokenResponseDto> loginMember(@RequestBody MemberLoginRequestDto memberLoginRequestDto);

    @PostMapping("/api/auth/reissue")
    ResponseEntity<TokenResponseDto> reissueToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto);
}
