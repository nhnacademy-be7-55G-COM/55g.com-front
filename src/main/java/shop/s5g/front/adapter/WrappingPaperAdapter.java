package shop.s5g.front.adapter;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import shop.s5g.front.config.FeignGatewayAuthorizationConfig;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;

@FeignClient(name = "wrappingPaper", url = "${gateway.url}", path = "/api/shop/wrapping-paper", configuration = FeignGatewayAuthorizationConfig.class)
public interface WrappingPaperAdapter {
    @GetMapping
    List<WrappingPaperResponseDto> fetchActivePapers(@RequestParam(value = "scope", required = false) String scope);

    @GetMapping("/{paperId}")
    WrappingPaperResponseDto fetchPaper(@PathVariable long paperId);

    @PostMapping
    ResponseEntity<WrappingPaperResponseDto> createPaper(@RequestBody WrappingPaperRequestDto createRequest);

    @DeleteMapping("/{paperId}")
    ResponseEntity<HttpStatus> deletePaper(@PathVariable long paperId);

}
