package shop.s5g.front.service.address;

import java.util.List;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;

public interface AddressService {
    List<AddressResponseDto> getAddresses();
    MessageDto deleteAddress(Long addressId);
    MessageDto updateAddress(Long addressId, AddressUpdateRequestDto addressUpdateRequestDto);
    MessageDto addAddress(AddressRequestDto addressRequestDto);
}
