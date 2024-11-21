package shop.s5g.front.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.service.category.CategoryService;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookRestController {

    private final CategoryService categoryService;

    @GetMapping("/categories/{categoryId}")
    public List<CategoryResponseDto> getChildCategories(@PathVariable Long categoryId) {
        return categoryService.getChildCategories(categoryId);
    }
}
