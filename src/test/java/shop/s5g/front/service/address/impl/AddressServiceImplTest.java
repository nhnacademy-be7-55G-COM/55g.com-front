package shop.s5g.front.service.address.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import shop.s5g.front.adapter.AddressAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;
import shop.s5g.front.exception.address.AddressNotFoundException;
import shop.s5g.front.exception.address.AddressRegisterFailedException;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressAdapter addressAdapter;

    @InjectMocks
    private AddressServiceImpl addressService;


    @Test
    void getAddresses_shouldReturnListOfAddresses() {
        // Given
        List<AddressResponseDto> mockResponse = Arrays.asList(
            new AddressResponseDto(1L, "123 Main St", "Apt 4B", "Home", true),
            new AddressResponseDto(2L, "456 Elm St", "Suite 1A", "Office", false)
        );
        when(addressAdapter.getAddresses()).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        List<AddressResponseDto> result = addressService.getAddresses();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).alias()).isEqualTo("Home");
    }

    @Test
    void getAddresses_shouldReturnEmptyListOnError() {
        // Given
        when(addressAdapter.getAddresses()).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        List<AddressResponseDto> result = addressService.getAddresses();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void deleteAddress_shouldReturnSuccessMessage() {
        // Given
        MessageDto mockResponse = new MessageDto("삭제 성공");
        when(addressAdapter.deleteAddress(1L)).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        MessageDto result = addressService.deleteAddress(1L);

        // Then
        assertThat(result.message()).isEqualTo("삭제 성공");
    }

    @Test
    void deleteAddress_shouldThrowAddressNotFoundExceptionOnError() {
        // Given
        when(addressAdapter.deleteAddress(1L)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When & Then
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress(1L));
    }

    @Test
    void updateAddress_shouldReturnSuccessMessage() {
        // Given
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("Updated Home", true);
        MessageDto mockResponse = new MessageDto("변경 성공");
        when(addressAdapter.updateAddress(any(Long.class), any(AddressUpdateRequestDto.class)))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        MessageDto result = addressService.updateAddress(1L, requestDto);

        // Then
        assertThat(result.message()).isEqualTo("변경 성공");
    }

    @Test
    void updateAddress_shouldThrowAddressNotFoundExceptionOnError() {
        // Given
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto("Updated Home", true);
        when(addressAdapter.updateAddress(any(Long.class), any(AddressUpdateRequestDto.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When & Then
        assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(1L, requestDto));
    }

    @Test
    void addAddress_shouldReturnSuccessMessage() {
        // Given
        AddressRequestDto requestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home", true);
        MessageDto mockResponse = new MessageDto("주소 저장 성공");
        when(addressAdapter.createAddress(any(AddressRequestDto.class)))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        MessageDto result = addressService.addAddress(requestDto);

        // Then
        assertThat(result.message()).isEqualTo("주소 저장 성공");
    }

    @Test
    void addAddress_shouldThrowAddressRegisterFailedExceptionOnError() {
        // Given
        AddressRequestDto requestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home", true);
        when(addressAdapter.createAddress(any(AddressRequestDto.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // When & Then
        assertThrows(AddressRegisterFailedException.class, () -> addressService.addAddress(requestDto));
    }
}