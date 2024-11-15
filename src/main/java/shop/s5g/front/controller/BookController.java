package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.service.book.BookService;


@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //모든 도서 목록 조회
    @GetMapping("/")
    public String bookByCategory(Model model, @PageableDefault(page = 0, size = 9, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {

        PageResponseDto<BookPageableResponseDto> allBooks = bookService.getAllBooks(pageable);

        int nowPage = pageable.getPageNumber() + 1;

        //시작 페이지
        int startPage = Math.max(nowPage - 4, 1);
        //마지막 페이지
        int endPage = Math.min(nowPage + 5, allBooks.totalPage());

        model.addAttribute("allBooks", allBooks.content());
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "index";
    }
}