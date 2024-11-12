package shop.s5g.front.service.address.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.AddressAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;
import shop.s5g.front.exception.address.AddressNotFoundException;
import shop.s5g.front.exception.address.AddressRegisterFailedException;
import shop.s5g.front.service.address.AddressService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressAdapter addressAdapter;

    @Override
    public List<AddressResponseDto> getAddresses() {
        try{
            ResponseEntity<List<AddressResponseDto>> responseEntity = addressAdapter.getAddresses();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public MessageDto deleteAddress(Long addressId) {
        try{
            ResponseEntity<MessageDto> responseEntity = addressAdapter.deleteAddress(addressId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
            throw new AddressNotFoundException("Address not found");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressNotFoundException(e.getMessage());
        }
    }

    @Override
    public MessageDto updateAddress(Long addressId, AddressUpdateRequestDto addressRequestDto) {
        try{
            ResponseEntity<MessageDto> responseEntity = addressAdapter.updateAddress(addressId, addressRequestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
            throw new AddressNotFoundException("Address not found");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressNotFoundException(e.getMessage());
        }
    }

    @Override
    public MessageDto addAddress(AddressRequestDto addressRequestDto) {
        try{
            ResponseEntity<MessageDto> responseEntity = addressAdapter.createAddress(addressRequestDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            }
            throw new AddressRegisterFailedException("address register failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressRegisterFailedException(e.getMessage());
        }
    }
}
