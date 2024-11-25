package shop.s5g.front.service.review.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.review.ReviewAdapter;
import shop.s5g.front.config.ComponentBuilderConfig;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.exception.review.ReviewRegisterFailedException;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.review.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewAdapter reviewAdapter;
    private final ImageService imageService;

    @Override
    public MessageDto addReview(CreateReviewRequestDto createReviewRequestDto) {
        try {
            List<String> imagePathList = new ArrayList<>();

            List<MultipartFile> attachmentList = createReviewRequestDto.attachment();
            for (int i = 1; i <= attachmentList.size(); i++) {
                MultipartFile file = attachmentList.get(i - 1);
                byte[] imageByte = file.getBytes();
                String filename = DigestUtils.md5Hex(imageByte);
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());

                String digestName = String.format("%s.%s", filename + "-" + i, extension);
                imageService.uploadImage(generateFilePath(digestName), imageByte);
                imagePathList.add(digestName);
            }

            ReviewRequestDto reviewRequestDto = ReviewRequestDto.from(createReviewRequestDto,
                imagePathList);

            ResponseEntity<MessageDto> response = reviewAdapter.registerReview(
                reviewRequestDto);

            if (response.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
                return response.getBody();
            }
            throw new ReviewRegisterFailedException("review register failed");

        } catch (IOException e) {
            throw new ApplicationException("이미지 업로드 도중 문제가 발생했습니다.");

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ReviewRegisterFailedException(e.getMessage());
        }
    }

    private String generateFilePath(String digestName) {
        return ComponentBuilderConfig.IMAGE_LOCATION_PATH + "review/" + digestName;
    }

    @Override
    public PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable) {
        return reviewAdapter.getReviewList(pageable);
    }
}
