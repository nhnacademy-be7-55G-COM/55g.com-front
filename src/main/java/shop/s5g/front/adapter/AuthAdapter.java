package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import shop.s5g.front.dto.auth.RoleResponseDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.dto.member.LoginRequestDto;

@FeignClient(name = "auth-service", url = "${gateway.url}")
public interface AuthAdapter {
    @PostMapping("/api/auth/login")
    ResponseEntity<TokenResponseDto> loginMember(@RequestBody LoginRequestDto loginRequestDto);

    @PostMapping("/api/auth/logout")
    ResponseEntity<Void> logoutMember(@RequestHeader(name = "Authorization") String refreshToken);

    @PostMapping("/api/auth/reissue")
    ResponseEntity<TokenResponseDto> reissueToken(@RequestHeader(name = "Authorization") String refreshToken);

    @PostMapping("/api/auth/admin/login")
    ResponseEntity<TokenResponseDto> loginAdmin(@RequestBody LoginRequestDto loginRequestDto);

    @GetMapping("/api/auth/role")
    ResponseEntity<RoleResponseDto> getRole(@RequestHeader(name = "Authorization") String accessToken);
}
