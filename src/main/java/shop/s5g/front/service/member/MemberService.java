package shop.s5g.front.service.member;

import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;

public interface MemberService {
    MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto);
    MemberInfoResponseDto getMemberInfo();
    MessageDto updateMember(MemberUpdateRequestDto updateMemberRequestDto);
}
