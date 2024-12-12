package shop.s5g.front.service.member.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.MemberAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.member.IdCheckResponseDto;
import shop.s5g.front.dto.member.MemberGradeResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.member.MemberStatusResponseDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.dto.member.PasswordChangeRequestDto;
import shop.s5g.front.exception.member.MemberGetInfoFailedException;
import shop.s5g.front.exception.member.MemberRegisterFailedException;
import shop.s5g.front.exception.member.MemberUpdateFailedException;
import shop.s5g.front.exception.member.PasswordChangeFailedException;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberAdapter memberAdapter;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void registerMember_Success() {
        // Arrange
        MemberRegistrationRequestDto requestDto = new MemberRegistrationRequestDto("test",
            "test@test.com", "test", "test", "01011111111", "19990909");
        MessageDto messageDto = new MessageDto("Registration successful");
        when(memberAdapter.registerMember(requestDto)).thenReturn(
            new ResponseEntity<>(messageDto, HttpStatus.OK));

        // Act
        MessageDto result = memberService.registerMember(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Registration successful", result.message());
        verify(memberAdapter, times(1)).registerMember(requestDto);
    }

    @Test
    void registerMember_Failure() {
        // Arrange
        MemberRegistrationRequestDto requestDto = new MemberRegistrationRequestDto("test",
            "test@test.com", "test", "test", "01011111111", "19990909");
        when(memberAdapter.registerMember(requestDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(MemberRegisterFailedException.class,
            () -> memberService.registerMember(requestDto));
        verify(memberAdapter, times(1)).registerMember(requestDto);
    }

    @Test
    void getMemberInfo_Success() {
        // Arrange
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto(
            12345L, // customerId
            new MemberStatusResponseDto(1L,"ACTIVE"),
            new MemberGradeResponseDto(1L,"GOLD", 100000, 1),
            "john_doe123", // loginId
            "encrypted_password_placeholder", // password
            "1985-06-15", // birth
            "John Doe", // name
            "john.doe@example.com", // email
            "+1234567890", // phoneNumber
            LocalDateTime.of(2023, 1, 15, 10, 30, 0), // createdAt
            LocalDateTime.of(2023, 12, 5, 22, 15, 0), // latestLoginAt
            5000L, // point
            List.of(
                new AddressResponseDto(
                    1L,
                    "123 Main Street",
                    "Springfield",
                    "IL",
                    true // isPrimary
                ),
                new AddressResponseDto(
                    2L,
                    "456 Oak Avenue",
                    "Hometown",
                    "TX",
                    false // isPrimary
                )
            ) // addresses
        );
        when(memberAdapter.getMemberInfo()).thenReturn(
            new ResponseEntity<>(responseDto, HttpStatus.OK));

        // Act
        MemberInfoResponseDto result = memberService.getMemberInfo();

        // Assert
        assertNotNull(result);
        verify(memberAdapter, times(1)).getMemberInfo();
    }

    @Test
    void getMemberInfo_Failure() {
        // Arrange
        when(memberAdapter.getMemberInfo()).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(MemberGetInfoFailedException.class, () -> memberService.getMemberInfo());
        verify(memberAdapter, times(1)).getMemberInfo();
    }

    @Test
    void getMemberInfoAsync_Success() throws ExecutionException, InterruptedException {
        // Arrange
        MemberInfoResponseDto responseDto = new MemberInfoResponseDto(
            12345L, // customerId
            new MemberStatusResponseDto(1L,"ACTIVE"),
            new MemberGradeResponseDto(1L,"GOLD", 100000, 1),
            "john_doe123", // loginId
            "encrypted_password_placeholder", // password
            "1985-06-15", // birth
            "John Doe", // name
            "john.doe@example.com", // email
            "+1234567890", // phoneNumber
            LocalDateTime.of(2023, 1, 15, 10, 30, 0), // createdAt
            LocalDateTime.of(2023, 12, 5, 22, 15, 0), // latestLoginAt
            5000L, // point
            List.of(
                new AddressResponseDto(
                    1L,
                    "123 Main Street",
                    "Springfield",
                    "IL",
                    true // isPrimary
                ),
                new AddressResponseDto(
                    2L,
                    "456 Oak Avenue",
                    "Hometown",
                    "TX",
                    false // isPrimary
                )
            ) // addresses
        );
        when(memberAdapter.getMemberInfo()).thenReturn(
            new ResponseEntity<>(responseDto, HttpStatus.OK));

        // Act
        var future = memberService.getMemberInfoAsync();

        // Assert
        assertNotNull(future.get());
        verify(memberAdapter, times(1)).getMemberInfo();
    }

    @Test
    void updateMember_Success() {
        // Arrange
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto("aaa", "aaa@email.com", "01022222222");
        MessageDto messageDto = new MessageDto("Update successful");
        when(memberAdapter.updateMember(requestDto)).thenReturn(
            new ResponseEntity<>(messageDto, HttpStatus.OK));

        // Act
        MessageDto result = memberService.updateMember(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Update successful", result.message());
        verify(memberAdapter, times(1)).updateMember(requestDto);
    }

    @Test
    void updateMember_Failure() {
        // Arrange
        MemberUpdateRequestDto requestDto = new MemberUpdateRequestDto("aaa", "aaa@email.com", "01022222222");
        when(memberAdapter.updateMember(requestDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(MemberUpdateFailedException.class,
            () -> memberService.updateMember(requestDto));
        verify(memberAdapter, times(1)).updateMember(requestDto);
    }

    @Test
    void isExistsLoginId_Success() {
        // Arrange
        IdCheckResponseDto responseDto = new IdCheckResponseDto(true);
        when(memberAdapter.checkId("testLoginId")).thenReturn(
            new ResponseEntity<>(responseDto, HttpStatus.OK));

        // Act
        boolean exists = memberService.isExistsLoginId("testLoginId");

        // Assert
        assertTrue(exists);
        verify(memberAdapter, times(1)).checkId("testLoginId");
    }

    @Test
    void changePassword_Success() {
        // Arrange
        PasswordChangeRequestDto requestDto = new PasswordChangeRequestDto("test", "test1");
        MessageDto messageDto = new MessageDto("Password changed");
        when(memberAdapter.changePassword(requestDto)).thenReturn(
            new ResponseEntity<>(messageDto, HttpStatus.OK));

        // Act
        MessageDto result = memberService.changePassword(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals("Password changed", result.message());
        verify(memberAdapter, times(1)).changePassword(requestDto);
    }

    @Test
    void changePassword_Failure() {
        // Arrange
        PasswordChangeRequestDto requestDto = new PasswordChangeRequestDto("test", "test1");
        when(memberAdapter.changePassword(requestDto)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(PasswordChangeFailedException.class,
            () -> memberService.changePassword(requestDto));
        verify(memberAdapter, times(1)).changePassword(requestDto);
    }

    @Test
    void activeMember_Success() {
        // Arrange
        String loginId = "testUser";
        MessageDto messageDto = new MessageDto("Activation successful");
        when(memberAdapter.changeActive(loginId)).thenReturn(
            new ResponseEntity<>(messageDto, HttpStatus.OK));

        // Act
        MessageDto result = memberService.activeMember(loginId);

        // Assert
        assertNotNull(result);
        assertEquals("Activation successful", result.message());
        verify(memberAdapter, times(1)).changeActive(loginId);
    }
}
