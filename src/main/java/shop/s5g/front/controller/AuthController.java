package shop.s5g.front.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.dto.member.LoginRequestDto;
import shop.s5g.front.dto.member.MemberActiveRequestDto;
import shop.s5g.front.exception.auth.InactiveException;
import shop.s5g.front.exception.member.InactiveCodeNotVaildatedException;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.member.InactiveService;
import shop.s5g.front.service.member.MemberService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CartService cartService;
    private final InactiveService inactiveService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto requestDto, HttpServletResponse response) {
        authService.loginMember(requestDto, response);

        return "cart/cartLogin";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        cartService.redisToDbWhenLogout();

        authService.logoutMember(request, response);
        return "redirect:/";
    }

    @ExceptionHandler(InactiveException.class)
    public String inactivePage(InactiveException e, Model model) {
        model.addAttribute("userId", e.getMessage());
        return "member/sleep";
    }

    @PostMapping("/member/change-status")
    public String activeMember(@ModelAttribute MemberActiveRequestDto requestDto, RedirectAttributes redirectAttributes) {
        if(!inactiveService.validateCode(requestDto.userId(), requestDto.code())){
            throw new InactiveException(requestDto.userId());
        }
        memberService.activeMember(requestDto.userId());
        redirectAttributes.addFlashAttribute("success", "휴면 해제! 다시 로그인 해주세요");
        return "redirect:/login";
    }

    @ExceptionHandler(InactiveCodeNotVaildatedException.class)
    public String inactiveCode(InactiveCodeNotVaildatedException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/login";
    }
}
