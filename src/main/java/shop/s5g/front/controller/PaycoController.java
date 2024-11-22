package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import shop.s5g.front.exception.payco.AlreadyLinkAccountException;
import shop.s5g.front.exception.payco.NeedLinkAccountException;
import shop.s5g.front.service.oauth.impl.PaycoService;

@Controller
@RequiredArgsConstructor
public class PaycoController {

    private final PaycoService paycoService;
    @Value("${oauth.payco.auth-url}")
    private String authUrl;
    @Value("${oauth.payco.client-id}")
    private String clientId;
    @Value("${oauth.payco.login-redirect-url}")
    private String loginCallbackUrl;
    @Value("${oauth.payco.link-redirect-url}")
    private String linkCallbackUrl;

    @GetMapping("/payco/login")
    public String login() {
        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("serviceProviderCode", "FRIENDS")
            .queryParam("userLocale", "ko_KR")
            .queryParam("redirect_uri", loginCallbackUrl)
            .build().toString();

        return "redirect:" + url;
    }

    @GetMapping("/linkAccount/payco")
    public String linkAccount(){
        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("serviceProviderCode", "FRIENDS")
            .queryParam("userLocale", "ko_KR")
            .queryParam("redirect_uri", linkCallbackUrl)
            .build().toString();

        return "redirect:" + url;
    }
    @GetMapping("/payco/callback")
    public String callback(@RequestParam(name = "code") String code, HttpServletResponse response) {
        paycoService.getToken(code, response);
        return "redirect:/";
    }

    @GetMapping("/payco/link")
    public String link(@RequestParam(name = "code") String code, RedirectAttributes redirectAttributes) {
        paycoService.linkAccount(code);
        redirectAttributes.addFlashAttribute("success", "연동에 성공했습니다");
        return "redirect:/mypage#personalInfo";
    }

    @ExceptionHandler(NeedLinkAccountException.class)
    public String handleLinkAccountException(NeedLinkAccountException e,
        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(AlreadyLinkAccountException.class)
    public String handleAlreadyLinkAccountException(AlreadyLinkAccountException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/mypage#personalInfo";
    }
}
