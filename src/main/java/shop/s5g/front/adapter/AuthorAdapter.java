package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    //작가 전체 조회
    @GetMapping("/api/shop/author")
    ResponseEntity<PageResponseDto<AuthorResponseDto>> getAllAuthors(Pageable pageable);

    //작가id로 작가 조회
    @GetMapping("/api/shop/author/{authorId}")
    ResponseEntity<AuthorResponseDto> findAuthor(@PathVariable("authorId") long authorId);

    //작가 수정
    @PutMapping("/api/shop/author/{authorId}")
    ResponseEntity<MessageDto> updateAuthor(@PathVariable("authorId") long authorId, @RequestBody AuthorRequestDto authorRequestDto);

    //작가 삭제(비활성화)
    @DeleteMapping("/api/shop/author/{authorId}")
    ResponseEntity<MessageDto> deleteAuthor(@PathVariable("authorId") long authorId);

    // 작가 이름 검색
    @GetMapping("/api/shop/authors")
    ResponseEntity<List<AuthorResponseDto>> searchAuthors(@RequestParam String keyword);
}
