package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.category.CategoryOneResponseDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.service.category.CategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    //카테고리 등록
    @GetMapping("/admin/category/register")
    public String showCategoryForm(Model model, @PageableDefault(page = 0, size = 999) Pageable pageable) {
//        model.addAttribute("parentCategories", categoryService.getParentCategories());
        model.addAttribute("parentCategories", categoryService.getKoreaCategories(pageable).content());
        return "category-register";
    }

    //카테고리 등록
    @PostMapping("/admin/category/register")
    //부모 카테고리 이름, 현재 카테고리 이름, 활성화 여부
    public String getCategory(@ModelAttribute CategoryRequestDto requestDto) {
        categoryService.addCategory(requestDto);
        return "redirect:/admin";
    }

    //전체 카테고리 조회
    @GetMapping("/category")
    @ResponseBody
    public List<CategoryResponseDto> allCategory(Model model, @PageableDefault(page = 0, size = 999) Pageable pageable) {
        PageResponseDto<CategoryResponseDto> koreaCategories = categoryService.getKoreaCategories(pageable);
        return koreaCategories.content();
    }

    //부모 카테고리 조회
    @GetMapping("/admin/category")
    public String adminCategoryForm(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        PageResponseDto<CategoryResponseDto> categories = categoryService.getKoreaCategories(pageable);
        int categoryNowPage = pageable.getPageNumber() + 1;
        int categoryStartPage = Math.max(categoryNowPage - 4, 1);
        int categoryEndPage = Math.min(categoryNowPage + 5, categories.totalPage());

        model.addAttribute("parentCategories", categories.content());
        model.addAttribute("categoryNowPage", categoryNowPage);
        model.addAttribute("categoryStartPage", categoryStartPage);
        model.addAttribute("categoryEndPage", categoryEndPage);
        return "category";
    }

    //하위 카테고리 조회
    @GetMapping("/admin/category/child/{categoryId}")
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
        return "category-list";
    }

    //카테고리 수정 페이지 이동
    @GetMapping("/admin/category/modify/{categoryId}")
    public String categoryModify(@PathVariable("categoryId") long categoryId, Model model) {
        CategoryOneResponseDto category = categoryService.getCategoryById(categoryId);
        model.addAttribute("modifyCategory", category);
        return "category-modify";
    }

    //카테고리 수정
    @PutMapping("/admin/category/update/{categoryId}")
    public String categoryUpdate(@PathVariable("categoryId") Long categoryId, @ModelAttribute CategoryRequestDto requestDto) {
        categoryService.updateCategory(categoryId, requestDto);
        return "redirect:/admin";
    }

    //카테고리 삭제(비활성화)
    @PostMapping("/admin/category/delete/{categoryId}")
    public String categoryDelete(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return "redirect:/admin";
    }
}