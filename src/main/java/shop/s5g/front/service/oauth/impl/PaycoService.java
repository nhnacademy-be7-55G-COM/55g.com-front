package shop.s5g.front.service.oauth.impl;

import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.PaycoAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.jwt.TokenResponseDto;
import shop.s5g.front.exception.AlreadyExistsException;
import shop.s5g.front.exception.ResourceNotFoundException;
import shop.s5g.front.exception.auth.MemberLoginFailedException;
import shop.s5g.front.exception.payco.AlreadyLinkAccountException;
import shop.s5g.front.exception.payco.LinkAccountFailedException;
import shop.s5g.front.exception.payco.NeedLinkAccountException;
import shop.s5g.front.service.oauth.OauthService;
import shop.s5g.front.utils.TokenUtils;

@Service
@RequiredArgsConstructor
public class PaycoService implements OauthService {

    private final PaycoAdapter paycoAdapter;

    @Override
    public void getToken(String code, HttpServletResponse response) {
        try{
            ResponseEntity<TokenResponseDto> responseEntity = paycoAdapter.getToken(code);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                TokenResponseDto tokenResponseDto = responseEntity.getBody();

                if (tokenResponseDto != null) {
                    TokenUtils.setTokenAtCookie(response, tokenResponseDto.accessToken(), tokenResponseDto.refreshToken(), 600, 1200);
                }
                return;
            }
            throw new MemberLoginFailedException("Member login failed");
        }
        catch (ResourceNotFoundException e) {
            throw new NeedLinkAccountException("최초 페이코 로그인 시 기존 계정과 연동이 필요합니다! 로그인 후 연동을 진행해주세요");
        }
        catch (FeignException e) {
            throw new MemberLoginFailedException(e.getMessage());
        }
    }

    @Override
    public String linkAccount(String code) {
        try{
            ResponseEntity<MessageDto> responseEntity = paycoAdapter.linkAccount(code);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                MessageDto messageDto = responseEntity.getBody();
                if (messageDto != null) {
                    return messageDto.message();
                }
            }
            throw new LinkAccountFailedException("Link account failed");
        }
        catch (AlreadyExistsException e){
            throw new AlreadyLinkAccountException("이미 연동된 계정입니다");
        }
        catch (FeignException e) {
            throw new LinkAccountFailedException(e.getMessage());
        }
    }
}
