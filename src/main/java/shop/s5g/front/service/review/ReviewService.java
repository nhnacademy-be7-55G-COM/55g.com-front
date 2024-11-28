package shop.s5g.front.service.review;

import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;

public interface ReviewService {

    MessageDto addReview(CreateReviewRequestDto createReviewRequestDto);

    PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable);
}
