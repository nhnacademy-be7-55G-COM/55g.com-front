package shop.s5g.front.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.front.adapter.TestAdapter;

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
}
