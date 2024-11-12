package shop.s5g.front.service.book.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.BookAdapter;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.exception.book.BookGetFailedException;
import shop.s5g.front.service.book.BookService;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookAdapter bookAdapter;

    /**
     * 모든 도서 조회
     */
    @Override
//    public Page<BookPageableResponseDto> getAllBooks(Pageable pageable) {
    public PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable) {
        try {
            ResponseEntity<PageResponseDto<BookPageableResponseDto>> allBooks = bookAdapter.getAllBooksPageable(pageable);
            if (allBooks.getStatusCode().is2xxSuccessful()) {
                return allBooks.getBody();
            }
            throw new BookGetFailedException("도서 목록이 존재하지 않습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new BookGetFailedException(e.getMessage());
        }
    }

    /**
     * 화면 확인용 데미 데이터
     */
//    @Override
//    public Page<BookPageableResponseDto> fake_getAllBooks(Pageable pageable) {
//
//        List<BookPageableResponseDto> books = List.of(new BookPageableResponseDto("A", 10000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("B", 20000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("C", 30000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("D", 40000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("E", 50000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("F", 60000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("G", 70000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("H", 80000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("I", 90000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("J", 100000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("K", 110000L, new BigDecimal("5.5")),
//                new BookPageableResponseDto("L", 120000L, new BigDecimal("5.5"))
//        );
//
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), books.size());
//        List<BookPageableResponseDto> pagedBooks = books.subList(start, end);
//
//        return new PageImpl<>(pagedBooks, pageable, books.size());
//    }
}
