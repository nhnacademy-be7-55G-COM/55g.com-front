package shop.s5g.front.service.category;

import java.util.List;

import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.category.CategoryDetailResponseDto;
import shop.s5g.front.dto.category.CategoryOneResponseDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;

public interface CategoryService {
    MessageDto addCategory(CategoryRequestDto requestDto);

//    List<CategoryResponseDto> getParentCategories();

    PageResponseDto<CategoryResponseDto> getKoreaCategories(Pageable pageable);

    List<CategoryResponseDto> getChildCategories(long categoryId);

    MessageDto updateCategory(Long categoryId, CategoryRequestDto requestDto);

    MessageDto delete(Long categoryId);

    CategoryOneResponseDto getCategoryById(long categoryId);
}
