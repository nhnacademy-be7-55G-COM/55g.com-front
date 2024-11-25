package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.tag.TagRequestDto;
import shop.s5g.front.dto.tag.TagResponseDto;


@FeignClient(value = "tag", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface TagAdapter {

    //태그 등록
    @PostMapping("/api/shop/tag")
    ResponseEntity<MessageDto> addTag(@RequestBody TagRequestDto tagDto);

    //모든 태그 조회
    @GetMapping("/api/shop/tag")
    ResponseEntity<PageResponseDto<TagResponseDto>> gatAllTags(Pageable pageable);

    //태그 삭제(비활성화)
    @DeleteMapping("/api/shop/tag/{tagId}")
    ResponseEntity<MessageDto> deleteTag(@PathVariable("tagId") Long tagId);
}
