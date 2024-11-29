package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;
import shop.s5g.front.service.publisher.PublisherService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PublisherController {
    public final PublisherService publisherService;

    //출판사 등록 뷰
    @GetMapping("/admin/publisher/register")
    public String publisherRegister() {
        return "publisher-register";
    }

    //출판사 등록
    @PostMapping("/admin/publisher/register")
    public String publisherRegister(@ModelAttribute PublisherRequestDto publisherRequestDto) {
        publisherService.addPublisher(publisherRequestDto);
        return "redirect:/admin";
    }

    //db의 모든 출판사 목록 가져오기
    @GetMapping("/admin/publisher/list")
    public String publisherList(Model model, @PageableDefault(page = 0, size = 9) Pageable pageable) {
        PageResponseDto<PublisherResponseDto> publisherList = publisherService.getPublisherList(pageable);
        int nowPage1 = pageable.getPageNumber() + 1;

        //시작 페이지
        int startPage1 = Math.max(nowPage1 - 4, 1);
        //마지막 페이지
        int endPage1 = Math.min(nowPage1 + 5, publisherList.totalPage());

        model.addAttribute("allPublisher", publisherList.content());
        model.addAttribute("nowPage1", nowPage1);
        model.addAttribute("startPage1", startPage1);
        model.addAttribute("endPage1", endPage1);
        return "publisher/publisher-list";
    }

    //출판사 수정 페이지 이동
    @GetMapping("/admin/publisher/modify/{id}")
    public String publishersModify(@PathVariable("id") Long id, Model model) {
        PublisherResponseDto publisher = publisherService.getPublisherById(id);
        model.addAttribute("modifyPublisher", publisher);
        return "publisher/publisher-modify";
    }

    //출판사 수정
    @PutMapping("/admin/publisher/update/{id}")
    public String publisherUpdate(@PathVariable("id") Long id, @ModelAttribute PublisherRequestDto publisherRequestDto) {
        publisherService.updatePublisher(id, publisherRequestDto);
        return "redirect:/admin";
    }

    //출판사 삭제(비활성화)
    @PostMapping("/admin/publisher/delete/{id}")
    public String publisherDelete(@PathVariable("id") Long id) {
        publisherService.delete(id);
        return "redirect:/admin/publisher/list";
    }
}