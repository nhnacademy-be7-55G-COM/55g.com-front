package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

@FeignClient(value = "category", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CategoryAdapter {

    //카테고리 등록
    @PostMapping("/api/shop/category")
    ResponseEntity<MessageDto> addsCategory(@RequestBody CategoryRequestDto categoryRequestDto);

    //카테고리 목록 조회
    @GetMapping("/category")
    ResponseEntity<List<CategoryResponseDto>> getAllCategories();
}