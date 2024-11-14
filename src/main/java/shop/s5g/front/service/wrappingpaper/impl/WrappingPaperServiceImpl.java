package shop.s5g.front.service.wrappingpaper.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import shop.s5g.front.adapter.WrappingPaperAdapter;
import shop.s5g.front.config.ComponentBuilderConfig;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperCreateRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.wrappingpaper.WrappingPaperService;

@RequiredArgsConstructor
@Service
@Slf4j
public class WrappingPaperServiceImpl implements WrappingPaperService {
    private final WrappingPaperAdapter wrappingPaperAdapter;
    private final ImageService imageService;

    private ObjectProvider<UriComponentsBuilder> builderProvider;

    @Autowired
    public void setBuilderProvider(
        @Qualifier("paperImageLocationBuilder")
        ObjectProvider<UriComponentsBuilder> builderProvider
    ) {
        this.builderProvider = builderProvider;
    }

    private static final String PAPER_PATH = ComponentBuilderConfig.IMAGE_LOCATION_PATH + "wrappingpaper";
    private static final String FILE_NAME_FORMAT = "%s.%s";
    public static final String SCOPE_ALL = "all";

    @Override
    @Async("purchaseRequest")
    public CompletableFuture<List<WrappingPaperResponseDto>> fetchActivePapersAsync() {
        return CompletableFuture.completedFuture(fetchActivePapers());
    }

    @Override
    public List<WrappingPaperResponseDto> fetchActivePapers() {
        return fetchPapers(null);
    }

    @Override
    public List<WrappingPaperResponseDto> fetchAllPapers() {
        return fetchPapers(SCOPE_ALL);
    }

    private List<WrappingPaperResponseDto> fetchPapers(String scope) {
        return wrappingPaperAdapter.fetchActivePapers(scope);
    }

    @Override
    public CompletableFuture<WrappingPaperResponseDto> fetchPaperAsync(long id) {
        return CompletableFuture.supplyAsync(
                () ->wrappingPaperAdapter.fetchPaper(id)
        );
    }

    @Override
    public WrappingPaperResponseDto createNewWrappingPaper(WrappingPaperCreateRequestDto request) {
        try {
            byte[] image = request.imageFile().getBytes();
            String hexDigest = DigestUtils.md5Hex(image);
            String extension = FilenameUtils.getExtension(request.imageFile().getOriginalFilename());

            WrappingPaperRequestDto requestDto = new WrappingPaperRequestDto(
                request.name(), request.price(), String.format(FILE_NAME_FORMAT, hexDigest, extension)
            );
            imageService.uploadImage(generateFilePath(hexDigest, extension), request.imageFile());
            // 40x, 50x가 나면 어차피 예외가 발생됨..
            return wrappingPaperAdapter.createPaper(requestDto).getBody();
        } catch (IOException e) {
            // TODO: 다른 예외로 바꾸기
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePaper(long id) {
        wrappingPaperAdapter.deletePaper(id);
    }

    private String generateFilePath(String name, String extension) {
        StringBuilder builder = new StringBuilder(PAPER_PATH);
        builder.append('/').append(name).append('.').append(extension);

        return builder.toString();
    }

    @Override
    public WrappingPaperView convertToView(WrappingPaperResponseDto response) {
        UriComponentsBuilder builder = builderProvider.getIfAvailable();
        String imageUri = builder.path("/" + response.imageName()).build().toUriString();

        return new WrappingPaperView(
            response.id(),
            response.active(),
            response.name(),
            response.price(),
            imageUri
        );
    }
}
