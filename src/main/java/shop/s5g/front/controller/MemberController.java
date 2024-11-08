package shop.s5g.front.controller;

import java.beans.PropertyEditorSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.dto.MemberRegistrationRequestDto;
import shop.s5g.front.service.member.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 생일(birthdate) 필드를 yyyyMMdd 형식의 문자열로 변환
        binder.registerCustomEditor(String.class, "birthDate", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // 날짜 형식 "yyyy-MM-dd"를 "yyyyMMdd"로 변경
                setValue(text != null ? text.replace("-", "") : null);
            }
        });
        // 전화번호 필드에 대한 커스텀 에디터 예시
        binder.registerCustomEditor(String.class, "phoneNumber", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                // 숫자만 남기기 (하이픈 제거)
                setValue(text.replaceAll("\\D", ""));
            }
        });
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MemberRegistrationRequestDto requestDto) {
        memberService.registerMember(requestDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
