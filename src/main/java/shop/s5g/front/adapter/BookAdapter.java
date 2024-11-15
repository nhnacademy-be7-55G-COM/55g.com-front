package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import shop.s5g.front.dto.BookRequestDto;
//import shop.s5g.front.dto.BookResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;

@FeignClient(value = "shop-service", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface BookAdapter {

    @GetMapping("/api/shop/book/{bookId}")
    ResponseEntity<BookDetailResponseDto> getBook(@PathVariable long bookId);

    //모든 도서 페이저블
    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<PageResponseDto<BookPageableResponseDto>> getAllBooksPageable(Pageable pageable);


}
