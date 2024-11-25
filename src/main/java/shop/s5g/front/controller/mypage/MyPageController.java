package shop.s5g.front.controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.dto.member.PasswordChangeRequestDto;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.service.address.AddressService;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.member.MemberService;

@Controller
@RequiredArgsConstructor
@RedirectWithAlert(title = "로그인 필요", redirect = "/login", exceptions = UnauthorizedException.class)
public class MyPageController {

    private final MemberService memberService;
    private final AddressService addressService;
    private final AuthService authService;
    private final CartService cartService;

    @PostMapping("/mypage/addAddress")
    public String registerAddress(@Valid @ModelAttribute AddressRequestDto addressRequestDto,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/mypage/address";
        }
        addressService.addAddress(addressRequestDto);
        return "redirect:/mypage/address";
    }

    @PostMapping("/mypage/deleteAddress")
    public String deleteAddress(@RequestParam Long addressId) {
        addressService.deleteAddress(addressId);
        return "redirect:/mypage/address";
    }

    @PostMapping("/mypage/updateAddress")
    public String updateAddress(@RequestParam Long addressId,
        @ModelAttribute AddressUpdateRequestDto requestDto, Model model) {
        addressService.updateAddress(addressId, requestDto);
        return "redirect:/mypage/address";
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {
//        return "mypage";
        return "layout/mypage";
    }

    @GetMapping("/mypage/personal-info")
    public String personalInfo(Model model) {
        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        model.addAttribute("member", responseDto);
        return "mypage/personal-info";
    }

    @GetMapping("/mypage/address")
    public String deliveryAddress(Model model) {
        List<AddressResponseDto> addresses = addressService.getAddresses();
        model.addAttribute("addresses", addresses);
        return "mypage/address";
    }

    @PostMapping("/mypage/changeInfo")
    public String changeMemberInfo(@Valid @ModelAttribute MemberUpdateRequestDto requestDto,
        BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "양식을 확인해주세요.");
            return "redirect:/mypage/personal-info";
        }
        memberService.updateMember(requestDto);
        redirectAttributes.addFlashAttribute("success","변경에 성공했습니다.");
        return "redirect:/mypage/personal-info";
    }

    @PostMapping("/mypage/changePassword")
    public String changePassword(@Valid @ModelAttribute PasswordChangeRequestDto requestDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "비밀번호 형식을 확인해주세요");
            return "redirect:/mypage/personal-info";
        }
        try{
            MessageDto messageDto = memberService.changePassword(requestDto);
            redirectAttributes.addFlashAttribute("success", messageDto.message());
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "비밀번호 변경을 실패했습니다.");
            return "redirect:/mypage/personal-info";
        }

        return "redirect:/mypage/personal-info";
    }

    @GetMapping("/mypage/grade")
    public String grade(Model model) {
        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        model.addAttribute("member", responseDto);
        return "mypage/grade";
    }

    @GetMapping("/mypage/deleteAccount")
    public String delete(){
        return "mypage/delete-account";
    }

    @PostMapping("/mypage/deleteAccount")
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        cartService.removeAccount();
        memberService.deleteMember();
        authService.logoutMember(request, response);

        return "redirect:/";
    }

}
