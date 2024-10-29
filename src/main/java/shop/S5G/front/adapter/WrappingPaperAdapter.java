package shop.S5G.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.S5G.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.S5G.front.dto.wrappingpaper.WrappingPaperResponseDto;

@FeignClient(name = "wrappingPaper", url = "${gateway.url}")
@RequestMapping("/api/shop/wrapping-paper")
public interface WrappingPaperAdapter {
    @GetMapping
    List<WrappingPaperResponseDto> fetchAllPapers();

    @GetMapping("/{paperId}")
    WrappingPaperResponseDto fetchPaper(@PathVariable long paperId);

    @PostMapping
    ResponseEntity<WrappingPaperResponseDto> createPaper(@RequestBody WrappingPaperRequestDto createRequest);

    @DeleteMapping("/{paperId}")
    ResponseEntity<HttpStatus> deletePaper(@PathVariable long paperId);

}
