package shop.s5g.front.service.book.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.book.BookDocumentAdapter;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;
import shop.s5g.front.service.book.BookDocumentService;

@Service
@RequiredArgsConstructor
public class BookDocumentServiceImpl implements BookDocumentService {
    private final BookDocumentAdapter bookDocumentAdapter;

    @Override
    public PageResponseDto<BookDocumentResponseDto> searchByKeyword(
        String keyword, Pageable pageable) {
        return bookDocumentAdapter.searchByKeyword(keyword, pageable);
    }

    @Override
    public PageResponseDto<BookDocumentResponseDto> searchByCategoryAndKeyword(String categoryName,
        String keyword, Pageable pageable) {
        return bookDocumentAdapter.searchByCategoryAndKeyword(categoryName, keyword, pageable);
    }
}
