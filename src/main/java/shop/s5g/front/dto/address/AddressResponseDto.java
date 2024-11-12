package shop.s5g.front.dto.address;

public record AddressResponseDto(
    Long addressId,

    String primaryAddress,

    String detailAddress,

    String alias,

    boolean isDefault
) {

}

