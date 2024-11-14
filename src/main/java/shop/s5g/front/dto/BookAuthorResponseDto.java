package shop.s5g.front.dto;

public record BookAuthorResponseDto(
    long authorId,
    String authorName,
    long authorTypeId,
    String typeName
) {

}
