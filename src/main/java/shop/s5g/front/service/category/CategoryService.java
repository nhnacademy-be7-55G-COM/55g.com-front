package shop.s5g.front.service.category;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    MessageDto addCategory(CategoryRequestDto requestDto);

    List<CategoryResponseDto> getParentCategories();
}
