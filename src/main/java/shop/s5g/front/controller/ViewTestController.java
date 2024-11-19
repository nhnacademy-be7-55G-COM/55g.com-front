package shop.s5g.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class ViewTestController {
    @GetMapping("/pending")
    public String pendingPageTest() {
        return "payments/pending";
    }

}
