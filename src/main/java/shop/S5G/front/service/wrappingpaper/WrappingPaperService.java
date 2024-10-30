package shop.S5G.front.service.wrappingpaper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import shop.S5G.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.S5G.front.dto.wrappingpaper.WrappingPaperResponseDto;

public interface WrappingPaperService {

    CompletableFuture<List<WrappingPaperResponseDto>> fetchAllPapersAsync();

    CompletableFuture<WrappingPaperResponseDto> fetchPaperAsync(long id);

    void createNewWrappingPaper(WrappingPaperRequestDto request);

    void deletePaper(long id);
}
