package shop.s5g.front.service.oauth.impl;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import shop.s5g.front.adapter.PaycoAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.auth.MemberLoginFailedException;
import shop.s5g.front.exception.payco.AlreadyLinkAccountException;
import shop.s5g.front.exception.payco.LinkAccountFailedException;
import shop.s5g.front.exception.payco.NeedLinkAccountException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaycoServiceTest {

    @Mock
    private PaycoAdapter paycoAdapter;

    @InjectMocks
    private PaycoService paycoService;

    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        response = new MockHttpServletResponse();
    }

    @Test
    void getToken_Success() {
        String code = "valid-code";
        TokenResponseDto tokenResponse = new TokenResponseDto("accessToken", "refreshToken");

        when(paycoAdapter.getToken(code)).thenReturn(ResponseEntity.ok(tokenResponse));

        paycoService.getToken(code, response);

        Cookie accessCookie = getCookieByName(response, "accessJwt");
        Cookie refreshCookie = getCookieByName(response, "refreshJwt");

        assertNotNull(accessCookie);
        assertEquals("accessToken", accessCookie.getValue());
        assertNotNull(refreshCookie);
        assertEquals("refreshToken", refreshCookie.getValue());
    }

    @Test
    void getToken_NeedLinkAccountException() {
        String code = "not-linked-code";
        when(paycoAdapter.getToken(code)).thenThrow(new ResourceNotFoundException("not found"));

        NeedLinkAccountException exception = assertThrows(NeedLinkAccountException.class, () -> paycoService.getToken(code, response));
        assertTrue(exception.getMessage().contains("연동"));
    }

    @Test
    void getToken_MemberLoginFailedException() {
        String code = "invalid-code";
        when(paycoAdapter.getToken(code)).thenThrow(FeignException.BadRequest.class);

        assertThrows(MemberLoginFailedException.class, () -> paycoService.getToken(code, response));
    }

    @Test
    void linkAccount_Success() {
        String code = "link-code";
        MessageDto messageDto = new MessageDto("Link successful!");
        when(paycoAdapter.linkAccount(code)).thenReturn(ResponseEntity.ok(messageDto));

        String resultMessage = paycoService.linkAccount(code);

        assertEquals("Link successful!", resultMessage);
    }

    @Test
    void linkAccount_AlreadyLinkAccountException() {
        String code = "already-linked-code";
        when(paycoAdapter.linkAccount(code)).thenThrow(new AlreadyExistsException("already exists"));

        AlreadyLinkAccountException exception = assertThrows(AlreadyLinkAccountException.class, () -> paycoService.linkAccount(code));
        assertTrue(exception.getMessage().contains("이미 연동된 계정입니다"));
    }

    @Test
    void linkAccount_LinkAccountFailedException() {
        String code = "fail-code";
        when(paycoAdapter.linkAccount(code)).thenThrow(FeignException.BadRequest.class);

        assertThrows(LinkAccountFailedException.class, () -> paycoService.linkAccount(code));
    }

    /**
     * MockHttpServletResponse에서 특정 이름의 쿠키를 찾아 반환하는 유틸리티 메서드
     */
    private Cookie getCookieByName(MockHttpServletResponse response, String name) {
        return Arrays.stream(response.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}
