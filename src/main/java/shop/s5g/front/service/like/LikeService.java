package shop.s5g.front.service.like;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.book.BookLikeResponseDto;
import java.util.List;

public interface LikeService {
    MessageDto getLike(Long bookId);

    MessageDto deleteLike(Long bookId);

    List<BookLikeResponseDto> getLikeByCustomer();
}
