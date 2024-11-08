package shop.S5G.front.controller;

import jakarta.validation.Valid;
import java.beans.PropertyEditorSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.S5G.front.dto.member.MemberInfoResponseDto;
import shop.S5G.front.dto.member.MemberRegistrationRequestDto;
import shop.S5G.front.dto.member.MemberUpdateRequestDto;
import shop.S5G.front.service.member.MemberService;

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
        binder.registerCustomEditor(String.class, "phoneNumber", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(text.replaceAll("\\D", ""));
            }
        });
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute MemberRegistrationRequestDto requestDto,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "회원가입 양식에 부합하지 않습니다");
            return "register";
        }
        memberService.registerMember(requestDto);
        return "redirect:/login";
    }
    @GetMapping("/mypage")
    public String myPage(Model model) {
        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        model.addAttribute("member", responseDto);
        return "mypage";
    }

    @PostMapping("/mypage/changeInfo")
    public String changeMemberInfo(@Valid @ModelAttribute MemberUpdateRequestDto requestDto,
        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "양식을 확인해주세요");
            return "mypage";
        }
        memberService.updateMember(requestDto);

        return "redirect:/mypage";
    }
}
