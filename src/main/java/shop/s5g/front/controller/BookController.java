package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.service.book.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //카테고리별 도서 목록 조회
    @GetMapping("/book/allList")
    public String bookByCategory(
            Model model,
            @PageableDefault(page = 0, size = 6) Pageable pageable,
            @RequestParam(value = "sortField", defaultValue = "title") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));

        Page<BookPageableResponseDto> allBooks = bookService.getAllBooks(sortedPageable);

//        Page<BookPageableResponseDto> allBooks = bookService.fake_getAllBooks(pageable);

        //현재 페이지(page)
        int nowPage = allBooks.getPageable().getPageNumber() + 1;
        //시작 페이지
        int startPage = Math.max(nowPage - 4, 1);
        //마지막 페이지
        int endPage = Math.min(nowPage + 5, allBooks.getTotalPages());

        model.addAttribute("allBooks", allBooks);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "book/book";
    }
}