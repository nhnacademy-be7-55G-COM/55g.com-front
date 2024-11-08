package shop.s5g.front.service.member;

import shop.s5g.front.dto.MemberRegistrationRequestDto;
import shop.s5g.front.dto.MessageDto;

public interface MemberService {
    MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto);
}
