package shop.s5g.front.service.like.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.LikeAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.book.BookLikeResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.customer.CustomerNotFoundException;
import shop.s5g.front.exception.like.LikeAlreadyExistException;
import shop.s5g.front.service.like.LikeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeAdapter likeAdapter;

    //좋아요 등록
    @Override
    public MessageDto getLike(Long bookId) {
        try {
            ResponseEntity<MessageDto> response = likeAdapter.addLike(bookId);
            return response.getBody();
        } catch (CustomerNotFoundException e) {
            throw new BadRequestException("로그인이 필요합니다." + e.getMessage());
        } catch (FeignException e) {
            if (e.status() == 409) {
                throw new LikeAlreadyExistException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    //좋아요 삭제
    @Override
    public MessageDto deleteLike(Long bookId) {
        try {
            ResponseEntity<MessageDto> response = likeAdapter.deleteLike(bookId);
            return response.getBody();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    //좋아요 누른 도서 정보
    @Override
    public List<BookLikeResponseDto> getLikeByCustomer() {
        try {
            ResponseEntity<List<BookLikeResponseDto>> response = likeAdapter.getBookByCustomerId();
            return response.getBody();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
