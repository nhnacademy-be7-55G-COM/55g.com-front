package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookAddRequestDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.BookNotFoundException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.category.CategoryService;
import shop.s5g.front.service.publisher.PublisherService;

@Controller
@RequiredArgsConstructor
public class BookUpdateController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;

    @GetMapping("/book/update/{bookId}")
    public ModelAndView updateBookForm(@PathVariable long bookId,
        @PageableDefault(page = 0, size = 9999) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView();
        BookDetailResponseDto dto = bookService.getBookDetail(bookId);

        modelAndView.addObject("book", dto);
        modelAndView.addObject("categoryList", categoryService.getKoreaCategories(pageable).content());
        modelAndView.addObject("publisherList", publisherService.getPublisherList(pageable).content());
        modelAndView.setViewName("book/update");
        return modelAndView;
    }

    @PostMapping("/book/update/{bookId}")
    public String updateBook(@PathVariable long bookId, BookAddRequestDto dto,
        @RequestBody MultipartFile thumbnailFile) {
        ResponseEntity<MessageDto> response = bookService.updateBook(bookId, dto, thumbnailFile);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "book/updateSuccess";
        } else if (response.getStatusCode().value() == 400) {
            throw new BadRequestException();
        } else if (response.getStatusCode().value() == 404) {
            throw new BookNotFoundException();
        }
        throw new RuntimeException();
    }
}
