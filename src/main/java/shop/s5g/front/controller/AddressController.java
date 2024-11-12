package shop.s5g.front.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.address.AddressRequestDto;
import shop.s5g.front.dto.address.AddressUpdateRequestDto;
import shop.s5g.front.service.address.AddressService;

@Controller
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/mypage/addAddress")
    public String registerAddress(@Valid @ModelAttribute AddressRequestDto addressRequestDto,
        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/mypage";
        }
        addressService.addAddress(addressRequestDto);
        return "redirect:/mypage";
    }

    @PostMapping("/mypage/deleteAddress")
    public String deleteAddress(@RequestParam Long addressId) {
        addressService.deleteAddress(addressId);
        return "redirect:/mypage";
    }

    @PostMapping("/mypage/updateAddress")
    public String updateAddress(@RequestParam Long addressId,
        @ModelAttribute AddressUpdateRequestDto requestDto, Model model) {
        addressService.updateAddress(addressId, requestDto);
        return "redirect:/mypage";
    }
}
