package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import shop.s5g.front.dto.book.BookAddRequestDto;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.category.CategoryService;
import shop.s5g.front.service.publisher.PublisherService;

@Controller
@RequiredArgsConstructor
public class BookAddController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final PublisherService publisherService;

    @GetMapping("/book/add")
    public ModelAndView bookAddForm(@PageableDefault(page = 0, size = 9999) Pageable pageable) {
        ModelAndView model = new ModelAndView();
        model.addObject("categoryList", categoryService.getKoreaCategories());
        model.addObject("publisherList", publisherService.getPublisherList(pageable).content());
        model.setViewName("book/add");
        return model;
    }

    @PostMapping("/book/add")
    public String bookAdd(BookAddRequestDto dto, @RequestBody MultipartFile thumbnailFile) {
        ResponseEntity<MessageDto> response = bookService.addBook(dto, thumbnailFile);

        if (response.getStatusCode().is2xxSuccessful()) {
            return "book/addSuccess";
        } else if (response.getStatusCode().value() == 400) {
            throw new BadRequestException();
        }

        throw new RuntimeException();
    }
}
