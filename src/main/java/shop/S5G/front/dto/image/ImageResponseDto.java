package shop.s5g.front.dto.image;

public record ImageResponseDto(
    String id,
    String url,
    String path,
    long bytes
) {

}
