package shop.S5G.front.service.member;

import shop.S5G.front.dto.MemberRegistrationRequestDto;
import shop.S5G.front.dto.MessageDto;

public interface MemberService {
    MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto);
}
