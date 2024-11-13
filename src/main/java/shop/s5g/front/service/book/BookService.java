package shop.s5g.front.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;

public interface BookService {
//    Page<BookPageableResponseDto> getAllBooks(Pageable pageable);
    PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable);

    List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIds);

//    CompletableFuture<List<BookSimpleResponseDto>> getSimpleBooksAsync(List<Long> bookIds);

//    CompletableFuture<List<BookSimpleResponseDto>> getSimpleBooksFromCartAsync(
//        List<CartBookInfoRequestDto> cart);

    List<BookSimpleResponseDto> getSimpleBooksFromCart(List<CartBookInfoRequestDto> cart);

//    Page<BookPageableResponseDto> fake_getAllBooks(Pageable pageable);
}
