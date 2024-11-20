package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.service.category.CategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //카테고리 등록
    @GetMapping("/category/register")
    public String showCategoryForm(Model model) {
//        model.addAttribute("parentCategories", categoryService.getParentCategories());
        model.addAttribute("parentCategories", categoryService.getKoreaCategories());
        return "category-register";
    }

    //카테고리 등록
    @PostMapping("/category/register")
    //부모 카테고리 이름, 현재 카테고리 이름, 활성화 여부
    public String getCategory(@ModelAttribute CategoryRequestDto requestDto) {
        categoryService.addCategory(requestDto);
        return "redirect:/";
    }

    //전체 카테고리 조회
    @GetMapping("/category")
    @ResponseBody
    public List<CategoryResponseDto> allCategory(Model model) {
        List<CategoryResponseDto> koreaCategories = categoryService.getKoreaCategories();
        return koreaCategories;
    }

    //부모 카테고리 조회
    @GetMapping("/admin/category")
    public String adminCategoryForm(Model model) {
        model.addAttribute("parentCategories", categoryService.getKoreaCategories());
        return "category";
    }

    //하위 카테고리 조회
    @GetMapping("/category/child/{categoryId}")
    @ResponseBody
    public List<CategoryResponseDto> childCategory(@PathVariable("categoryId") long categoryId, Model model) {
        List<CategoryResponseDto> childCategories = categoryService.getChildCategories(categoryId);
//        model.addAttribute("childCategories", childCategories);
        return childCategories;
    }

    //카테고리 상세
    @PostMapping("/admin/category/detail/{categoryId}")
    public String categoryDetail(@PathVariable("categoryId") long categoryId, Model model) {
        List<CategoryResponseDto> childCategories = categoryService.getChildCategories(categoryId);
        model.addAttribute("childCategories", childCategories);
        return "category-modify";
    }

    //카테고리 수정
    @PutMapping("/admin/category/update/{categoryId}")
    public String categoryUpdate(@PathVariable("categoryId") Long categoryId, @ModelAttribute CategoryRequestDto requestDto) {
        categoryService.updateCategory(categoryId, requestDto);
        return "redirect:/admin";
    }
}