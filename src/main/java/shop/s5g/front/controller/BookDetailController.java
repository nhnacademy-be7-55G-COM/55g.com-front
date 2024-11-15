package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.service.book.BookService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookDetailController {

    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ModelAndView getBook(@PathVariable(name = "bookId") long bookId) {
        ModelAndView modelAndView = new ModelAndView();
        BookDetailResponseDto dto = bookService.getBookDetail(bookId);

        modelAndView.addObject("book", dto);
        modelAndView.setViewName("book/detail");

        return modelAndView;
    }
}
