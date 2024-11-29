package shop.s5g.front.adapter.review;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.BackCreateReviewRequestDto;
import shop.s5g.front.dto.review.BackUpdateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;

@FeignClient(name = "review", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface ReviewAdapter {

    @PostMapping("/api/shop/review")
    ResponseEntity<MessageDto> registerReview(
        @RequestBody BackCreateReviewRequestDto backCreateReviewRequestDto);

    @GetMapping("/api/shop/review/list")
    PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable);

    @PatchMapping("/api/shop/review")
    ResponseEntity<MessageDto> updateReview(
        @RequestBody BackUpdateReviewRequestDto backUpdateReviewRequestDto);
}
