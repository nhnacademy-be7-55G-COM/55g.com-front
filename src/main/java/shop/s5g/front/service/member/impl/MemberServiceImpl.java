package shop.s5g.front.service.member.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.MemberAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.member.MemberInfoResponseDto;
import shop.s5g.front.dto.member.MemberRegistrationRequestDto;
import shop.s5g.front.dto.member.MemberUpdateRequestDto;
import shop.s5g.front.exception.member.MemberGetInfoFailedException;
import shop.s5g.front.exception.member.MemberRegisterFailedException;
import shop.s5g.front.exception.member.MemberUpdateFailedException;
import shop.s5g.front.service.member.MemberService;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberAdapter memberAdapter;

    @Override
    public MessageDto registerMember(MemberRegistrationRequestDto memberRegistrationRequestDto) {
        try{
            ResponseEntity<MessageDto> response = memberAdapter.registerMember(memberRegistrationRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("Member registration failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberRegisterFailedException(e.getMessage());
        }

    }

    @Override
    public MemberInfoResponseDto getMemberInfo() {
        try{
            ResponseEntity<MemberInfoResponseDto> response = memberAdapter.getMemberInfo();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberGetInfoFailedException("get member info failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberGetInfoFailedException("get member info failed");
        }
    }

    @Override
    public MessageDto updateMember(MemberUpdateRequestDto updateMemberRequestDto) {
        try{
            ResponseEntity<MessageDto> response = memberAdapter.updateMember(updateMemberRequestDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberUpdateFailedException("Member update failed");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberUpdateFailedException(e.getMessage());
        }
    }
}