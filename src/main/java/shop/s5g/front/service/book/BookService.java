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

public interface BookService {

    PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable);

    BookDetailResponseDto getBookDetail(long bookId);
}
