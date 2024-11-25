package shop.s5g.front.controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.s5g.front.annotation.MemberAndAdminOnly;
import shop.s5g.front.annotation.RedirectWithAlert;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressResponseDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;
import shop.s5g.front.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.front.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.dto.member.PasswordChangeRequestDto;
import shop.s5g.front.exception.auth.UnauthorizedException;
import shop.s5g.front.service.address.AddressService;
import shop.s5g.front.service.auth.AuthService;
import shop.s5g.front.service.cart.CartService;
import shop.s5g.front.service.coupon.coupon.CouponService;
import shop.s5g.front.service.coupon.user.UserCouponService;
import shop.s5g.front.service.member.MemberService;

@Controller
@RequiredArgsConstructor
@MemberAndAdminOnly
@RedirectWithAlert(title = "로그인 필요", redirect = "/login", exceptions = UnauthorizedException.class)
public class MyPageController {

    private final MemberService memberService;
    private final AddressService addressService;
    private final AuthService authService;
    private final CartService cartService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;

    @PostMapping("/mypage/addAddress")
    public String registerAddress(@Valid @ModelAttribute AddressRequestDto addressRequestDto,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/mypage#deliveryAddress";
        }
        addressService.addAddress(addressRequestDto);
        return "redirect:/mypage#deliveryAddress";
    }

    @PostMapping("/mypage/deleteAddress")
    public String deleteAddress(@RequestParam Long addressId) {
        addressService.deleteAddress(addressId);
        return "redirect:/mypage#deliveryAddress";
    }

    @PostMapping("/mypage/updateAddress")
    public String updateAddress(@RequestParam Long addressId,
        @ModelAttribute AddressUpdateRequestDto requestDto, Model model) {
        addressService.updateAddress(addressId, requestDto);
        return "redirect:/mypage#deliveryAddress";
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {

        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        List<AddressResponseDto> addresses = addressService.getAddresses();

        model.addAttribute("member", responseDto);
        model.addAttribute("addresses", addresses);

        return "layout/mypage";
    }

    @GetMapping("/mypage/coupons")
    public String couponPage(Model model) {

        Pageable pageable = PageRequest.of(0, 15);

        MemberInfoResponseDto responseDto = memberService.getMemberInfo();
        Page<ValidUserCouponResponseDto> couponList = userCouponService.getUserCoupons(
            responseDto.customerId(), pageable);
        Page<AvailableCouponResponseDto> availableCouponList = couponService.getAvailableCoupons(pageable);

        model.addAttribute("member", responseDto);
        model.addAttribute("couponList", couponList);
        model.addAttribute("availableCouponList", availableCouponList);

        model.addAttribute("usableCurrentPage", couponList.getPageable().getPageNumber());

        return "mypage/user-coupon";
    }

    @PostMapping("/mypage/changeInfo")
    public String changeMemberInfo(@Valid @ModelAttribute MemberUpdateRequestDto requestDto,
        BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "redirect:/mypage#personalInfo";
        }
        memberService.updateMember(requestDto);

        return "redirect:/mypage#personalInfo";
    }

    @PostMapping("/mypage/changePassword")
    public String changePassword(@Valid @ModelAttribute PasswordChangeRequestDto requestDto,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "비밀번호 형식을 확인해주세요");
            return "redirect:/mypage#personalInfo";
        }
        try{
            MessageDto messageDto = memberService.changePassword(requestDto);
            redirectAttributes.addFlashAttribute("success", messageDto.message());

        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "비밀번호 변경을 실패했습니다.");
            return "redirect:/mypage#personalInfo";
        }


        return "redirect:/mypage#personalInfo";
    }

    @PostMapping("/mypage/deleteAccount")
    public String deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        cartService.removeAccount();
        memberService.deleteMember();
        authService.logoutMember(request, response);
        return "redirect:/";
    }

    @PostMapping("/mypage/coupons/{customerId}")
    public String unUsedCoupons(
        @PathVariable("customerId") Long customerId,
        Pageable pageable, Model model) {



        return "redirect:/mypage#coupons";
    }

}
