package shop.s5g.front.adapter.dooray;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.message.DoorayMessageDto;

@FeignClient(name = "dooray", url = "https://hook.dooray.com/services/3204376758577275363/3946581390468309680/GMmHoyKxRmOqDAIjVwuQEw")
public interface DoorayAdapter {

    @PostMapping()
    ResponseEntity<Void> sendMessage(@RequestBody DoorayMessageDto messageDto);
}
