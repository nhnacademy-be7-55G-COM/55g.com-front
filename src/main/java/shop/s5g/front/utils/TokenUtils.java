package shop.s5g.front.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class TokenUtils {

    public static void setTokenAtCookie(HttpServletResponse response,
        String accessToken, String refreshToken, int accessTokenExpiredTime, int refreshTokenExpiredTime) {

        Cookie accessJwt = new Cookie("accessJwt", accessToken);
        accessJwt.setPath("/");
        accessJwt.setMaxAge(accessTokenExpiredTime);
        accessJwt.setHttpOnly(true);
        response.addCookie(accessJwt);

        Cookie refreshJwt = new Cookie("refreshJwt", refreshToken);
        refreshJwt.setPath("/");
        refreshJwt.setMaxAge(refreshTokenExpiredTime);
        refreshJwt.setHttpOnly(true);
        response.addCookie(refreshJwt);

    }
}
