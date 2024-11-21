package shop.s5g.front.dto.tag;

public record TagResponseDto(
        Long tagId,
        String tagName,
        boolean active
) {
}
