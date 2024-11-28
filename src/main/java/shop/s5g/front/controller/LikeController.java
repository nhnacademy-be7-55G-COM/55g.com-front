package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.s5g.front.dto.book.BookLikeResponseDto;
import shop.s5g.front.service.like.LikeService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //좋아요 페이지 이동
    @GetMapping("/mypage/like")
    public String myPage() {
        return "mypage/like-list";
    }

    //좋아요 조회
    @GetMapping("/mypage/like/list")
    @ResponseBody
    public List<BookLikeResponseDto> likeList(Model model) {
        //고객id로 도서id 리스트 반환
        List<BookLikeResponseDto> books = likeService.getLikeByCustomer();
        return books;
    }


    //좋아요 등록
//    @PostMapping("/mypage/like/{bookId}")
//    public String likeAdd(@PathVariable("bookId") Long bookId){
//        likeService.getLike(bookId);
//        //도서상세로 redirect
//        return "redirect:/mypage";
//    }

    //좋아요 등록
    @PostMapping("/mypage/like/{bookId}")
    public ResponseEntity<String> likeAdd(@PathVariable("bookId") Long bookId) {
        likeService.getLike(bookId);
        return ResponseEntity.ok("좋아요가 등록되었습니다.");
    }


    //좋아요 삭제
    @PostMapping("/mypage/like/delete/{bookId}")
    public String likeDelete(@PathVariable("bookId") Long bookId){
        likeService.deleteLike(bookId);
        //마이페이지로 redirect
        return "redirect:/mypage";
    }
}
