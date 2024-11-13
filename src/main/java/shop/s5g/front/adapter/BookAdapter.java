package shop.s5g.front.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.BookRequestDto;
import shop.s5g.front.dto.BookResponseDto;
import shop.s5g.front.dto.MessageDto;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;

@FeignClient(value = "shop-service", url = "${gateway.url}")
public interface BookAdapter {

    @GetMapping("/api/shop/book/{bookId}")
    ResponseEntity<BookDetailResponseDto> getBook(@PathVariable long bookId);

    @GetMapping("/api/shop/books")
    ResponseEntity<List<BookResponseDto>> getAllBooks();

    @PostMapping("/api/shop/book")
    ResponseEntity<MessageDto> addBook(@RequestBody BookRequestDto dto);

    @PutMapping("/api/shop/book/{bookId}")
    ResponseEntity<MessageDto> updateBook(@PathVariable long bookId, @RequestBody BookRequestDto dto);

    @DeleteMapping("/api/shop/book/{bookId}")
    ResponseEntity<MessageDto> deleteBook(@PathVariable long bookId);
    //모든 도서 페이저블
    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<PageResponseDto<BookPageableResponseDto>> getAllBooksPageable(Pageable pageable);
}
