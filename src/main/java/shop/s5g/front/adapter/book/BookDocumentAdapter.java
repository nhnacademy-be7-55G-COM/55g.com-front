package shop.s5g.front.adapter.book;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.book.BookDocumentResponseDto;

@FeignClient(name = "bookDocument", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface BookDocumentAdapter {
    @GetMapping("/api/shop/book/list")
    PageResponseDto<BookDocumentResponseDto> searchByKeyword(@RequestParam("keyword") String keyword, Pageable pageable);
}
