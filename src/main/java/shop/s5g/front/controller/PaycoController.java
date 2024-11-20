package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import shop.s5g.front.service.oauth.impl.PaycoService;

@Controller
@RequiredArgsConstructor
public class PaycoController {

    @Value("${oauth.payco.auth-url}")
    String authUrl;

    @Value("${oauth.payco.client-id}")
    String clientId;

    @Value("${oauth.payco.redirect-url}")
    String callbackUrl;

    private final PaycoService paycoService;

    @GetMapping("/payco/login")
    public String login() {
        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("serviceProviderCode", "FRIENDS")
            .queryParam("userLocale", "ko_KR")
            .queryParam("redirect_uri", callbackUrl)
            .build().toString();

        return "redirect:" + url;
    }


    @GetMapping("/payco/callback")
    public String callback(@RequestParam(name = "code") String code, HttpServletResponse response) {
        paycoService.getToken(code, response);
        return "redirect:/";
    }
}
