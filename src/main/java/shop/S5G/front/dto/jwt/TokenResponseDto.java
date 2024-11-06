package shop.S5G.front.dto.jwt;

public record TokenResponseDto(
    String accessToken,
    String refreshToken
) {

}
