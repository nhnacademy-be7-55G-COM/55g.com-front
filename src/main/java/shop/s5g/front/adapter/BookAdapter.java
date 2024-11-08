package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.book.BookResponseDto;


@FeignClient(value = "book", url = "${gateway.url}")
public interface BookAdapter {

    //모든 도서 조회
    @GetMapping("/books")
    ResponseEntity<Page<BookResponseDto>> getAllBooks(Pageable pageable);

    //모든 도서 페이저블
    @GetMapping("/books/pageable")
    ResponseEntity<Page<BookPageableResponseDto>> getAllBooksPage(Pageable pageable);
}