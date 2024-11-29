package shop.s5g.front.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.member.MemberLoginIdDto;
import shop.s5g.front.service.member.InactiveService;
import shop.s5g.front.service.message.MessageSenderService;

@RestController
@RequiredArgsConstructor
public class InactiveController {

    private final InactiveService inactiveService;
    private final MessageSenderService messageSenderService;

    @PostMapping("/request-code")
    public ResponseEntity<Void> requestCode(@RequestBody MemberLoginIdDto memberLoginIdDto) {
        String code = inactiveService.issueCode(memberLoginIdDto.userId());
        messageSenderService.sendMessage(code);

        return ResponseEntity.ok().build();
    }

}
