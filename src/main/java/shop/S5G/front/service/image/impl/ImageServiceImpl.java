package shop.s5g.front.service.image.impl;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.ImageProviderAdapter;
import shop.s5g.front.dto.image.ImageResponseDto;
import shop.s5g.front.service.image.ImageService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageProviderAdapter imageProviderAdapter;

    @Override
    public ImageResponseDto uploadImage(String path, byte[] image) {
        log.debug("Uploading image: path={}", path);
        return mapResponseToDto(imageProviderAdapter.uploadImage(path, image));
    }

    @Override
    public ImageResponseDto uploadImage(String path, MultipartFile file) {
        try {
            return uploadImage(path, file.getBytes());
        }catch (IOException e) {
            // TODO: 다른 예외로 교체하기.
            throw new RuntimeException(e);
        }
    }

    private ImageResponseDto mapResponseToDto(Map<String, Object> response) {
        @SuppressWarnings("unchecked")
        Map<String, Object> file = (Map<String, Object>) response.get("file");

        String url = (String) file.get("url");
        long bytes = ((Number) file.get("bytes")).longValue();
        String path = (String) file.get("path");
        String id = (String) file.get("id");

        return new ImageResponseDto(id, url, path, bytes);
    }
}
