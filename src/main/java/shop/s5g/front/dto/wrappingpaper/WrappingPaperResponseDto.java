package shop.s5g.front.dto.wrappingpaper;

public record WrappingPaperResponseDto(
    long id,
    boolean active,
    String name,
    int price,
    String imageName
) {
}
