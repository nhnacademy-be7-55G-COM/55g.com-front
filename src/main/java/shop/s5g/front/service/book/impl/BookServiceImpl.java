package shop.s5g.front.service.book.impl;

import feign.FeignException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.BookAdapter;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.BookNotFoundException;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.service.book.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookAdapter bookAdapter;

    public BookServiceImpl(BookAdapter bookAdapter) {
        this.bookAdapter = bookAdapter;
    }

//    @Override
//    public PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable) {
//        try{
//            ResponseEntity<PageResponseDto<BookPageableResponseDto>> allBooks = bookAdapter.getAllBooksPageable(pageable);
//            return allBooks.getBody();
//        }catch (FeignException e) {
//            throw new BookNotFoundException(e.getMessage());
//        }
//    }

    @Override
    public BookDetailResponseDto getBookDetail(long bookId) {
        try {
            ResponseEntity<BookDetailResponseDto> response = bookAdapter.getBook(bookId);
            return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BookNotFoundException();
            } else if (e.status() == 400) {
                throw new BadRequestException();
            }
        }

        throw new RuntimeException();
    }
    @Override
    public List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIds) {
        return bookAdapter.getSimpleBooks(bookIds);
    }

    @Override
    public List<BookSimpleResponseDto> getSimpleBooksFromCart(List<CartBookInfoRequestDto> cart) {
        List<Long> ids = cart.stream().map(CartBookInfoRequestDto::bookId).toList();
        return getSimpleBooks(ids);
    }
}
