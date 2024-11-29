package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.tag.TagRequestDto;
import shop.s5g.front.dto.tag.TagResponseDto;
import shop.s5g.front.service.tag.TagService;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    //태그 등록 뷰
    @GetMapping("/admin/tag/register")
    public String tegRegister() {
        return "tag/tag-register";
    }

    //태그 등록
    @PostMapping("/admin/tag/register")
    public String tegRegister(@ModelAttribute TagRequestDto tagRequestDto) {
        tagService.addTag(tagRequestDto);
        return "redirect:/admin";
    }

    //태그 목록 조회
    @GetMapping("/admin/tag/list")
    public String tegList(Model model, @PageableDefault(page = 0, size = 5) Pageable pageable) {

        PageResponseDto<TagResponseDto> allTags = tagService.getAllTag(pageable);
        int tagNowPage = pageable.getPageNumber() + 1;
        int tagStartPage = Math.max(tagNowPage - 4, 1);
        int tagEndPage = Math.min(tagNowPage + 5, allTags.totalPage());

        model.addAttribute("allTag", allTags.content());
        model.addAttribute("tagNowPage", tagNowPage);
        model.addAttribute("tagStartPage", tagStartPage);
        model.addAttribute("tagEndPage", tagEndPage);
        return "tag/tag-list";
    }

    //태그 삭제(비활성화)
    @PostMapping("/admin/tag/delete/{tagId}")
    public String tegDelete(@PathVariable("tagId") Long tagId) {
        tagService.delete(tagId);
        return "redirect:/admin/tag/list";
    }
}
