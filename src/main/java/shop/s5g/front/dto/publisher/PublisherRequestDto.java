package shop.s5g.front.dto.publisher;

import jakarta.validation.constraints.NotNull;

public record PublisherRequestDto(
        @NotNull
        String name
) {

}
