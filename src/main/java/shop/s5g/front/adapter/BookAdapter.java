package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;


@FeignClient(value = "book", url = "${gateway.url}")
public interface BookAdapter {

    //모든 도서 페이저블
    @GetMapping("/api/shop/books/pageable")
    ResponseEntity<PageResponseDto<BookPageableResponseDto>> getAllBooksPageable(Pageable pageable);
}