package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;
import shop.s5g.front.service.publisher.PublisherService;

@Controller
@RequiredArgsConstructor
public class PublisherController {
    public final PublisherService publisherService;

    //출판사 등록 뷰
    @GetMapping("/publisher/register")
    public String publisherRegister() {
        return "publisher-register";
    }

    //출판사 등록
    @PostMapping("/publisher/register")
    public String publisherRegister(@ModelAttribute PublisherRequestDto publisherRequestDto) {
        publisherService.addPublisher(publisherRequestDto);
        return "redirect:/";
    }

    //db의 모든 출판사 목록 가져오기
    @GetMapping("/publisher/list")
    public String publisherList(Model model) {
        model.addAttribute("allPublisher", publisherService.getPublisherList());
        return "publisher-list";
    }

    //출판사 수정 페이지 이동
    @GetMapping("/publisher/modify/{id}")
    public String publishersModify(@PathVariable("id") Long id, Model model) {
        PublisherResponseDto publisher = publisherService.getPublisherById(id);
        model.addAttribute("modifyPublisher", publisher);
        return "publisher-modify";
    }

    //출판사 수정
    @PutMapping("/publisher/update/{id}")
    public String publisherUpdate(@PathVariable("id") Long id, @ModelAttribute PublisherRequestDto publisherRequestDto) {

        //publisher -> publisherRequestDto로 바꿔야 함
        //publisher.setPublisherName(publisherRequestDto.getPublisherName());
        publisherService.updatePublisher(id, publisherRequestDto);
        return "redirect:/publisher/list";
    }
}