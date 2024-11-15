package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.s5g.front.dto.bookcategory.BookCategoryBookDto;
import shop.s5g.front.dto.bookcategory.BookCategoryResponseDto;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.service.bookcategory.BookCategoryService;
import shop.s5g.front.service.category.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;
    private final CategoryService categoryService;

    //선택한 카테고리의 도서 목록 조회
    @GetMapping("/category/{categoryId}")
    public String BookByCategoryId(@PathVariable("categoryId") Long categoryId,
                                   Model model) {

        //선택한 categoryId의 하위 카테고리 id List 가져오기
        List<CategoryResponseDto> childCategories = categoryService.getChildCategories(categoryId);

        //전체 도서카테고리 목록 가져오기
        List<BookCategoryResponseDto> allBookCategory = bookCategoryService.getAllBookCategory();

        //카테고리id List로 전체 bookCategory목록에서 해당 카테고리의 도서 선택하기
        List<BookCategoryResponseDto> bookList = new ArrayList<>();

        for (int i = 0; i < allBookCategory.size(); i++) {
            for (int j = 0; j < childCategories.size(); j++) {
                if (allBookCategory.get(i).categoryId() == childCategories.get(j).categoryId()) {
                    bookList.add(allBookCategory.get(i));
                }
            }
        }

        //bookId List로 book List 조회
//        List<BookCategoryResponseDto> bookList = bookCategoryService.getBookList(categoryId);

        //bookId로 book 리스트 반환
        List<BookCategoryBookDto> book = bookCategoryService.getBook(bookList);
        model.addAttribute("books", book);
        return "bookcategory-list";
    }
}