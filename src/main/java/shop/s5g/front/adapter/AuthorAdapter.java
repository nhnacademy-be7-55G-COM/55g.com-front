package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.author.AuthorRequestDto;
import shop.s5g.front.dto.author.AuthorResponseDto;

@FeignClient(value = "author", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface AuthorAdapter {

    //작가 등록
    @PostMapping("/api/shop/author")
    ResponseEntity<MessageDto> addAuthor(@RequestBody AuthorRequestDto authorRequestDto);

    //작가 조회
    @GetMapping("/api/shop/author")
    ResponseEntity<PageResponseDto<AuthorResponseDto>> getAllAuthors(Pageable pageable);
}
