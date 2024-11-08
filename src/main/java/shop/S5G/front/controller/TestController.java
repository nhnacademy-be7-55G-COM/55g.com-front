package shop.S5G.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import shop.S5G.front.adapter.TestAdapter;
import shop.S5G.front.annotation.RedirectWithAlert;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestAdapter testAdapter;

//    @GetMapping
//    public String testString() {
//        log.info("This is test log");
//        return "This is test string";
//    }

    @GetMapping
    public String test() {
        ResponseEntity<String> response = testAdapter.test();
        log.info(response.toString());
        return "test success";
    }

    @RedirectWithAlert(exceptions = RuntimeException.class, title = "테스트중 입니다.", redirect = "/")
    @GetMapping("/alert")
    public ModelAndView testAlertPage() {
        throw new IllegalStateException("테스트 예외 던짐");
    }

    @GetMapping("/auth")
    public String authTest() {
        return testAdapter.authTest().getBody();
    }
}
