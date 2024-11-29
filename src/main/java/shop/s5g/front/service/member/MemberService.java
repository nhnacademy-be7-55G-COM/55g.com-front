package shop.s5g.front.service.member;

import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.dto.member.PasswordChangeRequestDto;

public interface MemberService {
    MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto);
    MemberInfoResponseDto getMemberInfo();

    CompletableFuture<MemberInfoResponseDto> getMemberInfoAsync();

    MessageDto updateMember(MemberUpdateRequestDto updateMemberRequestDto);
    MessageDto deleteMember();
    boolean isExistsLoginId(String loginId);
    MessageDto changePassword(PasswordChangeRequestDto passwordChangeRequestDto);
    MessageDto activeMember(String loginId);
}
