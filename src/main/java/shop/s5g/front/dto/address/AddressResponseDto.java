package shop.s5g.front.dto.address;

import java.io.Serializable;

public record AddressResponseDto(
    Long addressId,

    String primaryAddress,

    String detailAddress,

    String alias,

    boolean isDefault
)implements Serializable {

    public String toString() {
        return String.format("%s %s", primaryAddress, detailAddress);
    }
}

