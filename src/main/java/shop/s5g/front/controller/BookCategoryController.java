package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.service.bookcategory.BookCategoryService;
import shop.s5g.front.service.category.CategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;
    private final CategoryService categoryService;

    //선택한 카테고리의 도서 목록 조회
//    @GetMapping("/category/{categoryId}")
//    public String BookByCategoryId(@PathVariable("categoryId") Long categoryId, Model model) {
//        List<BookPageableResponseDto> books = bookCategoryService.getBookByCategoryId(categoryId);
//        model.addAttribute("books", books);
//        return "bookcategory-list";
//    }
}