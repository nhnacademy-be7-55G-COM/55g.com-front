package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.author.AuthorRequestDto;
import shop.s5g.front.dto.author.AuthorResponseDto;
import shop.s5g.front.service.author.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    //작가 등록 뷰
    @GetMapping("/admin/author/register")
    public String authorRegister() {
        return "author-register";
    }

    //작가 등록
    @PostMapping("/admin/author/register")
    public String authorRegister(@ModelAttribute AuthorRequestDto authorRequestDto) {
        authorService.addAuthor(authorRequestDto);
        return "redirect:/admin";
    }

    //작가 목록 조회
    @GetMapping("/admin/author/list")
    public String authorList(Model model, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        PageResponseDto<AuthorResponseDto> allAuthors = authorService.getAllAuthor(pageable);
        int authorNowPage = pageable.getPageNumber() + 1;
        int authorStartPage = Math.max(authorNowPage - 4, 1);
        int authorEndPage = Math.min(authorNowPage + 5, allAuthors.totalPage());

        model.addAttribute("authors", allAuthors.content());
        model.addAttribute("authorNowPage", authorNowPage);
        model.addAttribute("authorStartPage", authorStartPage);
        model.addAttribute("authorEndPage", authorEndPage);
        return "author-list";
    }

    //작가 수정 페이지 이동
    @GetMapping("/admin/author/modify/{authorId}")
    public String authorModify(@PathVariable("authorId") long authorId, Model model) {
        AuthorResponseDto author = authorService.getAuthorById(authorId);
        model.addAttribute("modifyAuthor", author);
        return "author-modify";
    }

    //출판사 수정
    @PutMapping("/admin/author/update/{authorId}")
    public String authorUpdate(@PathVariable("authorId") long authorId, @ModelAttribute AuthorRequestDto authorRequestDto) {
        authorService.updateAuthor(authorId, authorRequestDto);
        return "redirect:/admin/author/list";
    }

    //작가 삭제(비활성화)
    @PostMapping("/admin/author/delete/{authorId}")
    public String authorDelete(@PathVariable("authorId") long authorId) {
        authorService.deleteAuthor(authorId);
        return "redirect:/admin/author/list";
    }
}
