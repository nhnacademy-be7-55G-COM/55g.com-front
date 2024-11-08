package shop.s5g.front.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.book.BookPageableResponseDto;

public interface BookService {
    Page<BookPageableResponseDto> getAllBooks(Pageable pageable);

//    Page<BookPageableResponseDto> fake_getAllBooks(Pageable pageable);
}
