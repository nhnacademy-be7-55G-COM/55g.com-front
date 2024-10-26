package shop.S5G.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.S5G.front.dto.MemberRegistrationRequestDto;

@Controller
public class MemberController {

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegistrationRequestDto requestDto) {
        return "login";
    }

}
