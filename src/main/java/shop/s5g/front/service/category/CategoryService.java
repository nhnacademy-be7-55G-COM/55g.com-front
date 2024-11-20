package shop.s5g.front.service.category;

import java.util.List;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.category.CategoryDetailResponseDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

public interface CategoryService {
    MessageDto addCategory(CategoryRequestDto requestDto);

//    List<CategoryResponseDto> getParentCategories();

    List<CategoryResponseDto> getKoreaCategories();

    List<CategoryResponseDto> getChildCategories(long categoryId);

    MessageDto updateCategory(Long categoryId, CategoryRequestDto requestDto);
}
