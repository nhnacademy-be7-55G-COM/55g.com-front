package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.bookcategory.BookCategoryBookDto;
import shop.s5g.front.dto.bookcategory.BookCategoryResponseDto;

import java.util.List;

@FeignClient(value = "bookcategory", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface BookCategoryAdapter {

    //카테고리 아이디에 해당하는 도서 조회
//    @GetMapping("/api/shop/bookcategory/{categoryId}")
//    ResponseEntity<List<BookCategoryResponseDto>> getBookCategory(@PathVariable("categoryId") Long categoryId);

    //bookId 리스트로 book 리스트 조회
//    @GetMapping("/api/shop/book/{bookIdList}")
//    ResponseEntity<List<BookCategoryBookDto>> getBooks(@PathVariable("bookIdList") List<BookCategoryResponseDto> bookList);

//    //bookcategory 전체 조회
//    @GetMapping("/api/shop/bookcategory")
//    ResponseEntity<List<BookCategoryResponseDto>> getAllBookCategory();

    //categoryId로 book_List 조회
    @GetMapping("/api/shop/books/{categoryId}")
    ResponseEntity<List<BookPageableResponseDto>> getBookByCategory(@PathVariable("categoryId") Long categoryId);
}