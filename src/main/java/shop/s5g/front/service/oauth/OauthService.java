package shop.s5g.front.service.oauth;

import jakarta.servlet.http.HttpServletResponse;

public interface OauthService {
    void getToken(String code, HttpServletResponse response);
    String linkAccount(String code);
}
