package shop.S5G.front.service.category;

import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.dto.category.CategoryRequestDto;
import shop.S5G.front.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    MessageDto addCategory(CategoryRequestDto requestDto);

    List<CategoryResponseDto> getParentCategories();
}
