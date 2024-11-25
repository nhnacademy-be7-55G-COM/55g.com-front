package shop.s5g.front.service.review;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;

public interface ReviewService {
    MessageDto addReview(CreateReviewRequestDto createReviewRequestDto);
}
