package shop.s5g.front.service.bookcategory.impl;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.BookCategoryAdapter;
import shop.s5g.front.dto.bookcategory.BookCategoryBookDto;
import shop.s5g.front.dto.bookcategory.BookCategoryResponseDto;
import shop.s5g.front.exception.book.BookCategoryNotFoundException;
import shop.s5g.front.service.bookcategory.BookCategoryService;

import java.util.List;

@Service
public class BookCategoryServiceImpl implements BookCategoryService {

    public final BookCategoryAdapter bookCategoryAdapter;
    public BookCategoryServiceImpl(BookCategoryAdapter bookCategoryAdapter) {
        this.bookCategoryAdapter = bookCategoryAdapter;
    }

    @Override
    public List<BookCategoryResponseDto> getBookList(Long categoryId) {
        try{
            ResponseEntity<List<BookCategoryResponseDto>> bookCategory = bookCategoryAdapter.getBookCategory(categoryId);
            return bookCategory.getBody();
        }catch (FeignException e){
            throw new BookCategoryNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<BookCategoryBookDto> getBook(List<BookCategoryResponseDto> bookList) {
        try{
            ResponseEntity<List<BookCategoryBookDto>> books = bookCategoryAdapter.getBooks(bookList);
            return books.getBody();
        }catch (FeignException e){
            throw new BookCategoryNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<BookCategoryResponseDto> getAllBookCategory() {
        try{
            ResponseEntity<List<BookCategoryResponseDto>> bookCategory = bookCategoryAdapter.getAllBookCategory();
            return bookCategory.getBody();
        }
        catch (FeignException e){
            throw new BookCategoryNotFoundException(e.getMessage());
        }
    }
}