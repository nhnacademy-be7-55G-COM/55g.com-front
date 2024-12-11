package shop.s5g.front.service.auth.impl;

import feign.FeignException;
import feign.FeignException.BadRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import shop.s5g.front.adapter.AuthAdapter;
import shop.s5g.front.dto.auth.RoleResponseDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.dto.member.LoginRequestDto;
import shop.s5g.front.exception.AuthenticationException;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.auth.InactiveException;
import shop.s5g.front.exception.auth.LogoutException;
import shop.s5g.front.exception.auth.MemberLoginFailedException;
import shop.s5g.front.exception.auth.TokenNotFoundException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthAdapter authAdapter;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequestDto loginRequestDto;
    private MockHttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto("testUser", "testPassword");
        mockResponse = new MockHttpServletResponse();
    }

    @Test
    void loginMember_Success() {
        TokenResponseDto tokenResponseDto = new TokenResponseDto("access-token", "refresh-token");
        when(authAdapter.loginMember(any())).thenReturn(ResponseEntity.ok(tokenResponseDto));

        authService.loginMember(loginRequestDto, mockResponse);

        Cookie accessCookie = getCookieByName(mockResponse, "accessJwt");
        Cookie refreshCookie = getCookieByName(mockResponse, "refreshJwt");

        assertNotNull(accessCookie);
        assertEquals("access-token", accessCookie.getValue());
        assertNotNull(refreshCookie);
        assertEquals("refresh-token", refreshCookie.getValue());
    }

    @Test
    void loginMember_InactiveException() {
        when(authAdapter.loginMember(any())).thenThrow(new BadRequestException("User inactive"));

        assertThrows(InactiveException.class, () -> authService.loginMember(loginRequestDto, mockResponse));
    }

    @Test
    void loginMember_MemberLoginFailedException() {
        when(authAdapter.loginMember(any())).thenThrow(new AuthenticationException("Invalid credentials"));

        assertThrows(MemberLoginFailedException.class, () -> authService.loginMember(loginRequestDto, mockResponse));
    }

    @Test
    void loginAdmin_Success() {
        TokenResponseDto tokenResponseDto = new TokenResponseDto("admin-access-token", "admin-refresh-token");
        when(authAdapter.loginAdmin(any())).thenReturn(ResponseEntity.ok(tokenResponseDto));

        authService.loginAdmin(loginRequestDto, mockResponse);

        Cookie accessCookie = getCookieByName(mockResponse, "accessJwt");
        Cookie refreshCookie = getCookieByName(mockResponse, "refreshJwt");

        assertNotNull(accessCookie);
        assertEquals("admin-access-token", accessCookie.getValue());
        assertNotNull(refreshCookie);
        assertEquals("admin-refresh-token", refreshCookie.getValue());
    }

    @Test
    void loginAdmin_MemberLoginFailedException() {
        when(authAdapter.loginAdmin(any())).thenThrow(FeignException.BadRequest.class);

        assertThrows(MemberLoginFailedException.class, () -> authService.loginAdmin(loginRequestDto, mockResponse));
    }

    @Test
    void logoutMember_Success() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("refreshJwt", "refresh-token-value"));
        when(authAdapter.logoutMember(any())).thenReturn(ResponseEntity.ok().build());

        authService.logoutMember(request, mockResponse);

        Cookie accessCookie = getCookieByName(mockResponse, "accessJwt");
        Cookie refreshCookie = getCookieByName(mockResponse, "refreshJwt");

        assertNotNull(accessCookie);
        assertNull(accessCookie.getValue());
        assertNotNull(refreshCookie);
        assertNull(refreshCookie.getValue());
    }

    @Test
    void logoutMember_TokenNotFound() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // no refreshJwt cookie set
        assertThrows(TokenNotFoundException.class, () -> authService.logoutMember(request, mockResponse));
    }

    @Test
    void logoutMember_LogoutException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("refreshJwt", "refresh-token-value"));

        when(authAdapter.logoutMember(any())).thenThrow(FeignException.BadRequest.class);

        assertThrows(LogoutException.class, () -> authService.logoutMember(request, mockResponse));
    }

    @Test
    void reissueToken_Success() {
        String oldRefreshToken = "old-refresh-token";
        TokenResponseDto newTokens = new TokenResponseDto("new-access-token", "new-refresh-token");
        when(authAdapter.reissueToken(any())).thenReturn(ResponseEntity.ok(newTokens));

        authService.reissueToken(oldRefreshToken, mockResponse);

        Cookie accessCookie = getCookieByName(mockResponse, "accessJwt");
        Cookie refreshCookie = getCookieByName(mockResponse, "refreshJwt");

        assertNotNull(accessCookie);
        assertEquals("new-access-token", accessCookie.getValue());
        assertNotNull(refreshCookie);
        assertEquals("new-refresh-token", refreshCookie.getValue());
    }

    @Test
    void reissueToken_BadRequestException() {
        String oldRefreshToken = "old-refresh-token";
        when(authAdapter.reissueToken(any())).thenThrow(new BadRequestException("invalid token"));

        authService.reissueToken(oldRefreshToken, mockResponse);

        // Should clear cookies
        Cookie refreshCookie = getCookieByName(mockResponse, "refreshJwt");

        assertEquals(null, refreshCookie.getValue());
    }

    @Test
    void reissueToken_MemberLoginFailedException() {
        String oldRefreshToken = "old-refresh-token";
        when(authAdapter.reissueToken(any())).thenThrow(FeignException.BadRequest.class);

        assertThrows(MemberLoginFailedException.class, () -> authService.reissueToken(oldRefreshToken, mockResponse));
    }

    @Test
    void getRole_Success() {
        RoleResponseDto roleResponseDto = new RoleResponseDto("ADMIN");
        when(authAdapter.getRole(any())).thenReturn(ResponseEntity.ok(roleResponseDto));

        String role = authService.getRole("access-token");
        assertEquals("ADMIN", role);
    }

    @Test
    void getRole_RoleGetInfoFailedException() {
        when(authAdapter.getRole(any())).thenThrow(FeignException.BadRequest.class);
        assertThrows(BadRequest.class, () -> authService.getRole("access-token"));
    }

    /**
     * 특정 이름을 가진 쿠키를 MockHttpServletResponse에서 찾아 반환하는 헬퍼 메서드
     */
    private Cookie getCookieByName(MockHttpServletResponse response, String name) {
        return Arrays.stream(response.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst()
            .orElse(null);
    }
}
