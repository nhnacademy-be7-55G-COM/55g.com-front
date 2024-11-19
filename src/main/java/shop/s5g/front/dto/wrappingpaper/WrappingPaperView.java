package shop.s5g.front.dto.wrappingpaper;

import java.io.Serializable;

public record WrappingPaperView (
    long id,
    boolean active,
    String name,
    int price,
    String imageLink
) implements Serializable {

}
