package shop.s5g.front.controller.mypage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageSubController {
    @GetMapping("/point-history")
    public String viewPointHistory() {
        return "mypage/point-history";
    }
}
