package shop.s5g.front.service.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.review.ReviewAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;
import shop.s5g.front.exception.review.ReviewRegisterFailedException;
import shop.s5g.front.service.review.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewAdapter reviewAdapter;

    @Override
    public MessageDto addReview(CreateReviewRequestDto createReviewRequestDto) {
        try {
            ResponseEntity<MessageDto> response = reviewAdapter.registerReview(
                createReviewRequestDto);
            if (response.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
                return response.getBody();
            }
            throw new ReviewRegisterFailedException("review register failed");

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ReviewRegisterFailedException(e.getMessage());
        }
    }

    @Override
    public PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable) {
        return reviewAdapter.getReviewList(pageable);
    }
}
