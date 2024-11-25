package shop.s5g.front.dto.customer;

import java.io.Serializable;

public record CustomerResponseDto(
    Long customerId,
    String password,
    String name,
    String phoneNumber,
    String email
) implements Serializable {
}
