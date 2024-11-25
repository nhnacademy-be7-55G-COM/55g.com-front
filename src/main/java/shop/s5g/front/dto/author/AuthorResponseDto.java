package shop.s5g.front.dto.author;

public record AuthorResponseDto(
        long authorId,
        String name,
        boolean active
) {
}
