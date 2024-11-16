package shop.s5g.front.service.bookcategory;

import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.bookcategory.BookCategoryBookDto;
import shop.s5g.front.dto.bookcategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryService {
//    List<BookCategoryResponseDto> getBookList(Long categoryId);
//
//    List<BookCategoryBookDto> getBook(List<BookCategoryResponseDto> bookList);

//    List<BookCategoryResponseDto> getAllBookCategory();

    List<BookPageableResponseDto> getBookByCategoryId(Long categoryId);
}
