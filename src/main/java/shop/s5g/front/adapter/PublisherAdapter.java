package shop.s5g.front.adapter;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;

import java.util.List;

@FeignClient(value = "publisher", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface PublisherAdapter {

    //출판사 등록 db에 전달
    @PostMapping("/api/shop/publisher")
    ResponseEntity<MessageDto> addPublisher(@RequestBody PublisherRequestDto publisherRequestDto);

    //전체 출판사 가져옴
    @GetMapping("/api/shop/publisher")
    ResponseEntity<PageResponseDto<PublisherResponseDto>> getAllPublisher(Pageable pageable);

    //id로 출판사 객체 조회
    @GetMapping("/api/shop/publisher/{publisherId}")
    ResponseEntity<PublisherResponseDto> findPublisher(@Valid @PathVariable("publisherId") Long publisherId);

    //출판사 수정
    @PutMapping("/api/shop/publisher/{publisherId}")
    ResponseEntity<MessageDto> updatePublisher(@Valid @PathVariable("publisherId") Long publisherId, @Valid @RequestBody PublisherRequestDto publisherRequestDto);

    //출판사 삭제(비활성화)
    @DeleteMapping("/api/shop/publisher/{publisherId}")
    ResponseEntity<MessageDto> deletePublisher(@Valid @PathVariable("publisherId") Long publisherId);
}
