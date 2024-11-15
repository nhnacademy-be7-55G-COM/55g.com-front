package shop.s5g.front.dto.jwt;

public record TokenResponseDto(
    String accessToken,
    String refreshToken
) {

}
