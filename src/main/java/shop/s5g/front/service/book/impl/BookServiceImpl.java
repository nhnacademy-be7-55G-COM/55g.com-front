package shop.s5g.front.service.book.impl;

import feign.FeignException;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.BookAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.BookDetailResponseDto;
import shop.s5g.front.dto.book.BookPageableResponseDto;
import shop.s5g.front.dto.book.BookAddRequestDto;
import shop.s5g.front.dto.book.BookAddSendDto;
import shop.s5g.front.dto.image.ImageResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.BookNotFoundException;
import shop.s5g.front.dto.book.BookSimpleResponseDto;
import shop.s5g.front.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.front.service.book.BookService;
import shop.s5g.front.service.image.ImageService;

@Service
public class BookServiceImpl implements BookService {

    private final BookAdapter bookAdapter;
    private final ImageService imageService;

    public BookServiceImpl(BookAdapter bookAdapter, ImageService imageService) {
        this.bookAdapter = bookAdapter;
        this.imageService = imageService;
    }

    @Override
    public PageResponseDto<BookPageableResponseDto> getAllBooks(Pageable pageable) {
        try{
            ResponseEntity<PageResponseDto<BookPageableResponseDto>> allBooks = bookAdapter.getAllBooksPageable(pageable);
            return allBooks.getBody();
        }catch (FeignException e) {
            throw new BookNotFoundException(e.getMessage());
        }
    }

    @Override
    public BookDetailResponseDto getBookDetail(long bookId) {
        try {
            ResponseEntity<BookDetailResponseDto> response = bookAdapter.getBook(bookId);
            return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BookNotFoundException();
            } else if (e.status() == 400) {
                throw new BadRequestException();
            }
        }

        throw new RuntimeException();
    }

    @Override
    public List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIds) {
        return bookAdapter.getSimpleBooks(bookIds);
    }

    @Override
    public List<BookSimpleResponseDto> getSimpleBooksFromCart(List<CartBookInfoRequestDto> cart) {
        List<Long> ids = cart.stream().map(CartBookInfoRequestDto::bookId).toList();
        return getSimpleBooks(ids);
    }

    @Override
    public ResponseEntity<MessageDto> addBook(BookAddRequestDto dto, MultipartFile file) {
        String filePath = null;

        try {
            if (file != null && !file.isEmpty()) {
                ImageResponseDto imageDto = imageService.uploadImage(
                    "/55gshop/book/thumbnails/" + file.getOriginalFilename(), file.getBytes());
                filePath = file.getOriginalFilename();
            }

            BookAddSendDto newDto = BookAddSendDto.of(dto, filePath);
            return bookAdapter.addBook(newDto);
        } catch (FeignException e) {
            if (e.status() == 400) {
                throw new BadRequestException();
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<MessageDto> updateBook(long bookId, BookAddRequestDto dto,
        MultipartFile file) {
        String filePath = null;

        try {
            if (file != null && !file.isEmpty()) {
                ImageResponseDto imageDto = imageService.uploadImage(
                    "/55gshop/book/thumbnails/" + file.getOriginalFilename(), file.getBytes());
                filePath = file.getOriginalFilename();
            }

            BookAddSendDto newDto = BookAddSendDto.of(dto, filePath);
            return bookAdapter.updateBook(bookId, newDto);
        } catch (FeignException e) {
            if (e.status() == 400) {
                throw new BadRequestException();
            }
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //도서 삭제

//    @Override
//    public MessageDto deleteBook(Long bookId) {
//        try {
//            MessageDto response =  bookAdapter.deleteBook(bookId);
//            return response;
//        }catch (FeignException e) {
//            if (e.status() == 400) {
//                throw new BadRequestException("도서 삭제에 실패했습니다.");
//            }
//        }
//        throw new RuntimeException();
//    }
}
