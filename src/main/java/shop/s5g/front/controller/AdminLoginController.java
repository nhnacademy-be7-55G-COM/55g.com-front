package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.dto.member.LoginRequestDto;
import shop.s5g.front.service.auth.AuthService;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {

    private final AuthService authService;

    @GetMapping("/admin/login")
    public String login() {
        return "admin/admin-login";
    }

    @PostMapping("/admin/login")
    public String loginPost(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {
        authService.loginAdmin(requestDto, response);
        return "redirect:/admin";
    }
}
