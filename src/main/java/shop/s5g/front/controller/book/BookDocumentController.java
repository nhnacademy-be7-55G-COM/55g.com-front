package shop.s5g.front.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;
import shop.s5g.front.service.book.BookDocumentService;

@Controller
@RequiredArgsConstructor
public class BookDocumentController {
    private final BookDocumentService bookDocumentService;

    @GetMapping("/book/search")
    public String searchByKeyword(@RequestParam(required = false) String keyword, @PageableDefault(size = 12) Pageable pageable, Model model) {
        if (keyword == null) {
            keyword = "";
        }

        PageResponseDto<BookDocumentResponseDto> searchBookList = bookDocumentService.searchByKeyword(keyword, pageable);
        model.addAttribute("books", searchBookList.content());
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("totalPage", searchBookList.totalPage());
        model.addAttribute("pageSize", searchBookList.pageSize());
        model.addAttribute("totalElements", searchBookList.totalElements());

        return "book/searchBookList";
    }
}
