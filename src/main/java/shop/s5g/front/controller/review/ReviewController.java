package shop.s5g.front.controller.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
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
        return "redirect:/review/list";
    }
}
