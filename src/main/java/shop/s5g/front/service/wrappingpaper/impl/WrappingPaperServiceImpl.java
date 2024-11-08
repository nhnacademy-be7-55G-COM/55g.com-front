package shop.s5g.front.service.wrappingpaper.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.WrappingPaperAdapter;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RequiredArgsConstructor
@Service
@Slf4j
public class WrappingPaperServiceImpl implements WrappingPaperService {
    private final WrappingPaperAdapter wrappingPaperAdapter;

    @Override
    public CompletableFuture<List<WrappingPaperResponseDto>> fetchAllPapersAsync() {
        return CompletableFuture.supplyAsync(wrappingPaperAdapter::fetchAllPapers);
    }

    @Override
    public CompletableFuture<WrappingPaperResponseDto> fetchPaperAsync(long id) {
        return CompletableFuture.supplyAsync(() -> wrappingPaperAdapter.fetchPaper(id));
    }

    @Override
    public void createNewWrappingPaper(WrappingPaperRequestDto request) {
        // 40x, 50x가 나면 어차피 예외가 발생됨..
        wrappingPaperAdapter.createPaper(request);
    }

    @Override
    public void deletePaper(long id) {
        wrappingPaperAdapter.deletePaper(id);
    }
}
