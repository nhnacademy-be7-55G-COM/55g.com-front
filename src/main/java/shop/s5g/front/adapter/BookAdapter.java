package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.book.BookSimpleResponseDto;


@FeignClient(value = "book", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface BookAdapter {

    //모든 도서 페이저블
    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<PageResponseDto<BookPageableResponseDto>> getAllBooksPageable(Pageable pageable);

    @GetMapping("/api/shop/books/query")
    List<BookSimpleResponseDto> getSimpleBooks(@RequestParam List<Long> books);

}