package shop.s5g.front.dto.wrappingpaper;

import java.io.Serializable;

public record WrappingPaperResponseDto(
    long id,
    boolean active,
    String name,
    int price,
    String imageName
) implements Serializable {
}
