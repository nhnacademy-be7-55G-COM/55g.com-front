package shop.S5G.front.service.member;

import shop.S5G.front.dto.member.MemberInfoResponseDto;
import shop.S5G.front.dto.member.MemberRegistrationRequestDto;
import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.dto.member.MemberUpdateRequestDto;

public interface MemberService {
    MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto);
    MemberInfoResponseDto getMemberInfo();
    MessageDto updateMember(MemberUpdateRequestDto updateMemberRequestDto);
}
