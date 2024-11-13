package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.dto.member.LoginRequestDto;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.service.cart.CartService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CartService cartService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {
        authService.loginMember(requestDto, response);

        return "redirect:/cart/loginProcess";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        cartService.redisToDbWhenLogout();

        authService.logoutMember(request, response);
        return "redirect:/";
    }
}
