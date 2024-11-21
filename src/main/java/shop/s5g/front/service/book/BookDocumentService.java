package shop.s5g.front.service.book;

import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;

public interface BookDocumentService {
    PageResponseDto<BookDocumentResponseDto> searchByKeyword(String keyword, Pageable pageable);
}
