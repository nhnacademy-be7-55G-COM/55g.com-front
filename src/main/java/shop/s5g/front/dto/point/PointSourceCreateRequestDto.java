package shop.s5g.front.dto.point;

import jakarta.validation.constraints.NotBlank;

public record PointSourceCreateRequestDto(
    @NotBlank
    String pointSourceName
) {


}
