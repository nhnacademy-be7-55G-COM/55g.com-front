package shop.s5g.front.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;
import shop.s5g.front.dto.review.UpdateReviewRequestDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.service.review.ReviewService;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public String registerReview(
        @Valid @ModelAttribute CreateReviewRequestDto createReviewRequestDto,
        BindingResult bindingResult) {

        // TODO: 리뷰 등록 폼에서 실패 메세지 띄우기
        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        reviewService.addReview(createReviewRequestDto);
        return "redirect:/mypage/review-list";
    }

    @GetMapping("/mypage/review-list")
    public String getReviewList(
        @PageableDefault(size = 10, sort = "reviewAt", direction = Direction.DESC) Pageable pageable,
        Model model) {
        PageResponseDto<ReviewResponseDto> reviewList = reviewService.getReviewList(pageable);

        model.addAttribute("reviewList", reviewList.content());
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("totalPage", reviewList.totalPage());
        model.addAttribute("pageSize", reviewList.pageSize());
        model.addAttribute("totalElements", reviewList.totalElements());
        return "mypage/review-list";
    }

    @PatchMapping("/review")
    public String updateReview(
        @Valid @ModelAttribute UpdateReviewRequestDto updateReviewRequestDto,
        BindingResult bindingResult) {

        // TODO: 리뷰 수정 폼에서 실패 메세지 띄우기
        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        reviewService.updateReview(updateReviewRequestDto);
        return "redirect:/mypage/review-list";
    }
}
