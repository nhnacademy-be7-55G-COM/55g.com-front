package shop.s5g.front.dto.tag;

import jakarta.validation.constraints.NotNull;

public record TagRequestDto(

        @NotNull
        String tagName
) {
}
