package shop.s5g.front.dto.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CouponTemplateRegisterRequestDto(

    @NotNull
    @Min(1)
    Long couponPolicyId,

    @NotBlank
    @Length(min = 2,max = 50)
    String couponName,

    @NotBlank
    @Length(min = 1, max = 200)
    String couponDescription
) {
}
