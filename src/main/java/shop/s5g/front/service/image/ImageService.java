package shop.s5g.front.service.image;

import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.dto.image.ImageResponseDto;

public interface ImageService {

    ImageResponseDto uploadImage(String path, byte[] image);

    ImageResponseDto uploadImage(String path, MultipartFile file);
}
