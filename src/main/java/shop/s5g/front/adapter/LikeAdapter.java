package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.book.BookLikeResponseDto;

import java.util.List;

@FeignClient(value = "like", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface LikeAdapter {

    //좋아요 등록
    @PostMapping("/api/shop/like/{bookId}")
    ResponseEntity<MessageDto> addLike(@PathVariable Long bookId);

    //좋아요 삭제
    @DeleteMapping("/api/shop/like/{bookId}")
    ResponseEntity<MessageDto> deleteLike(@PathVariable Long bookId);

    //마이페이지에서 좋아요 누른 도서 목록 확인
    @GetMapping("/api/shop/like")
    ResponseEntity<List<BookLikeResponseDto>> getBookByCustomerId();
}
