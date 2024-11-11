package shop.s5g.front.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

import java.util.List;

@FeignClient(value = "category", url = "${gateway.url}")
public interface CategoryAdapter {

    //카테고리 등록
    @PostMapping("/api/shop/category")
    ResponseEntity<MessageDto> addsCategory(@RequestBody CategoryRequestDto categoryRequestDto);

    //카테고리 목록 조회
    @GetMapping("/api/shop/category")
    ResponseEntity<List<CategoryResponseDto>> getAllCategories();

    //자식 카테고리 조회
    @GetMapping("/api/shop/category/childCategory/{categoryId}")
    ResponseEntity<List<CategoryResponseDto>> getChildCategories(@PathVariable("categoryId") Long categoryId);

    //국내도서 하위 카테고리 조회
    @GetMapping("/api/shop/category/korea")
    ResponseEntity<List<CategoryResponseDto>> getKoreaCategories();
}