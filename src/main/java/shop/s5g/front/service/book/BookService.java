package shop.s5g.front.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
//import shop.s5g.front.dto.BookRequestDto;
//import shop.s5g.front.dto.BookResponseDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;

public interface BookService {

    PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable);

    List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIds);

//    CompletableFuture<List<BookSimpleResponseDto>> getSimpleBooksAsync(List<Long> bookIds);

//    CompletableFuture<List<BookSimpleResponseDto>> getSimpleBooksFromCartAsync(
//        List<CartBookInfoRequestDto> cart);

    List<BookSimpleResponseDto> getSimpleBooksFromCart(List<CartBookInfoRequestDto> cart);

//    Page<BookPageableResponseDto> fake_getAllBooks(Pageable pageable);
    BookDetailResponseDto getBookDetail(long bookId);
}
