package shop.s5g.front.service.wrappingpaper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;

public interface WrappingPaperService {

    CompletableFuture<List<WrappingPaperResponseDto>> fetchAllPapersAsync();

    CompletableFuture<WrappingPaperResponseDto> fetchPaperAsync(long id);

    void createNewWrappingPaper(WrappingPaperRequestDto request);

    void deletePaper(long id);
}
