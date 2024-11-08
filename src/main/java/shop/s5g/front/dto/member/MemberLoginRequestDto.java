package shop.s5g.front.dto.member;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberLoginRequestDto(
    @NotBlank
    @Length(min = 4, max = 30)
    String loginId,

    @NotBlank
    @Length(min = 5, max = 30)
    String password
) {

}
