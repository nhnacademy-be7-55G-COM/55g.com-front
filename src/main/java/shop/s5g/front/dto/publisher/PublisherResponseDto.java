package shop.s5g.front.dto.publisher;

import jakarta.validation.constraints.NotNull;

public record PublisherResponseDto(
        @NotNull
        Long id,
        @NotNull
        String name,
        @NotNull
        boolean active
) {
}
