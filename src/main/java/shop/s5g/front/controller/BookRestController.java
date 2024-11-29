package shop.s5g.front.controller;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.dto.author.AuthorResponseDto;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.dto.tag.TagResponseDto;
import shop.s5g.front.service.author.AuthorService;
import shop.s5g.front.service.category.CategoryService;
import shop.s5g.front.service.tag.TagService;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookRestController {

    private final CategoryService categoryService;
    private final TagService tagService;
    private final AuthorService authorService;

    @GetMapping("/categories/{categoryId}")
    public List<CategoryResponseDto> getChildCategories(@PathVariable Long categoryId) {
        return categoryService.getChildCategories(categoryId);
    }

    @GetMapping("/author/search")
    public ResponseEntity<List<AuthorResponseDto>> searchAuthor(@NotNull String keyword){
        return authorService.searchAuthors(keyword);
    }

    @GetMapping("/tag/search")
    public ResponseEntity<List<TagResponseDto>> searchTags(@NotNull String keyword){
        return tagService.searchTags(keyword);
    }
}
