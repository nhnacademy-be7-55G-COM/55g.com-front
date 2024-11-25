package shop.s5g.front.adapter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.category.CategoryOneResponseDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

@FeignClient(value = "category", url = "${gateway.url}", configuration = FeignGatewayAuthorizationConfig.class)
public interface CategoryAdapter {

    //카테고리 등록
    @PostMapping("/api/shop/category")
    ResponseEntity<MessageDto> addsCategory(@RequestBody CategoryRequestDto categoryRequestDto);

    //카테고리 목록 조회
    @GetMapping("/api/shop/category")
    ResponseEntity<List<CategoryResponseDto>> getAllCategories();

    //국내도서 하위 카테고리 조회
    @GetMapping("/api/shop/category/korea")
    ResponseEntity<PageResponseDto<CategoryResponseDto>> getKoreaCategories(Pageable pageable);

    //자식 카테고리 조회
    @GetMapping("/api/shop/category/childCategory/{categoryId}")
    ResponseEntity<List<CategoryResponseDto>> getChildCategories(@PathVariable("categoryId") long categoryId);

    //카테고리 id로 카테고리 조회
    @GetMapping("/api/shop/admin/coupons/category/{categoryId}")
    ResponseEntity<CategoryOneResponseDto> findCategoryById(@PathVariable("categoryId") Long categoryId);

    //카테고리 수정
    @PutMapping("/api/shop/category/{categoryId}")
    ResponseEntity<MessageDto> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryRequestDto requestDto);

    //카테고리 삭제(비활성화)
    @DeleteMapping("/api/shop/category/{categoryId}")
    ResponseEntity<MessageDto> deleteCategory(@PathVariable("categoryId") Long categoryId);
}