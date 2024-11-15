package shop.s5g.front.service.wrappingpaper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperCreateRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;

public interface WrappingPaperService {

    CompletableFuture<List<WrappingPaperResponseDto>> fetchActivePapersAsync();

    List<WrappingPaperResponseDto> fetchActivePapers();

    List<WrappingPaperResponseDto> fetchAllPapers();

    CompletableFuture<WrappingPaperResponseDto> fetchPaperAsync(long id);

    WrappingPaperResponseDto createNewWrappingPaper(WrappingPaperCreateRequestDto request);

    void deletePaper(long id);

    WrappingPaperView convertToView(WrappingPaperResponseDto response);
}
