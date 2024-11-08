package shop.S5G.front.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.front.dto.book.BookPageableResponseDto;

public interface BookService {
    Page<BookPageableResponseDto> getAllBooks(Pageable pageable);

//    Page<BookPageableResponseDto> fake_getAllBooks(Pageable pageable);
}
