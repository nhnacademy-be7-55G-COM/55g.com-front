package shop.s5g.front.dto.wrappingpaper;

public record WrappingPaperView(
    long id,
    boolean active,
    String name,
    int price,
    String imageLink
) {

}
