package shop.s5g.front.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.dooray.DoorayAdapter;
import shop.s5g.front.dto.message.DoorayMessageDto;

@Service
@RequiredArgsConstructor
public class MessageSenderService {

    private final DoorayAdapter doorayAdapter;

    public void sendMessage(String code) {
        DoorayMessageDto messageDto = new DoorayMessageDto("휴면 인증 코드",
            "https://static.dooray.com/static_images/dooray-bot.png", code);

        doorayAdapter.sendMessage(messageDto);
    }
}
