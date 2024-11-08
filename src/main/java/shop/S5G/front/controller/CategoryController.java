package shop.S5G.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shop.S5G.front.dto.category.CategoryRequestDto;
import shop.S5G.front.service.category.CategoryService;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //DB의 모든 카테고리 이름 가져오기(등록)
    @GetMapping("/category/register")
    public String showCategoryForm(Model model) {
        model.addAttribute("parentCategories", categoryService.getParentCategories());
        return "category-register";
    }

    //카테고리 등록
    @PostMapping("/category")
    //부모 카테고리 이름, 현재 카테고리 이름, 활성화 여부
    public String getCategory(@ModelAttribute CategoryRequestDto requestDto) {
        categoryService.addCategory(requestDto);
        return "redirect:/";
    }

    //전체 카테고리 조회
    @GetMapping("/category")
    public String allCategory(Model model) {
        model.addAttribute("allCategories", categoryService.getParentCategories());
        return "category";
    }
}