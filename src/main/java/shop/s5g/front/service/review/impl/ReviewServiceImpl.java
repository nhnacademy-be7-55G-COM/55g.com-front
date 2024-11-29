package shop.s5g.front.service.review.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.review.ReviewAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.BackCreateReviewRequestDto;
import shop.s5g.front.dto.review.BackUpdateReviewRequestDto;
import shop.s5g.front.dto.review.CreateReviewRequestDto;
import shop.s5g.front.dto.review.ReviewResponseDto;
import shop.s5g.front.dto.review.UpdateReviewRequestDto;
import shop.s5g.front.exception.review.ReviewRegisterFailedException;
import shop.s5g.front.exception.review.ReviewUpdateFailedException;
import shop.s5g.front.service.review.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewAdapter reviewAdapter;

    @Override
    public MessageDto addReview(CreateReviewRequestDto createReviewRequestDto) {
        BackCreateReviewRequestDto backCreateReviewRequestDto = (BackCreateReviewRequestDto) changeMultipartFileToByte(
            createReviewRequestDto.validFiles(), createReviewRequestDto);
        ResponseEntity<MessageDto> response = reviewAdapter.registerReview(
            backCreateReviewRequestDto);

        if (response.getStatusCode().equals(HttpStatus.CREATED)) {
            return response.getBody();
        }
        throw new ReviewRegisterFailedException("review register failed");
    }

    private Object changeMultipartFileToByte(
        List<MultipartFile> validFiles, Object reviewDto) {

        try {
            List<byte[]> imageByteList = new ArrayList<>();
            List<String> extensions = new ArrayList<>();

            for (int i = 1; i <= validFiles.size(); i++) {
                MultipartFile file = validFiles.get(i - 1);
                byte[] imageByte = file.getBytes();
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());

                imageByteList.add(imageByte);
                extensions.add(extension);
            }

            if (reviewDto instanceof CreateReviewRequestDto createDto) {
                return BackCreateReviewRequestDto.from(createDto, imageByteList, extensions);
            } else if (reviewDto instanceof UpdateReviewRequestDto updateDto) {
                return BackUpdateReviewRequestDto.from(updateDto, imageByteList, extensions);
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 도중 문제가 발생했습니다.");
        }
    }

    @Override
    public PageResponseDto<ReviewResponseDto> getReviewList(Pageable pageable) {
        return reviewAdapter.getReviewList(pageable);
    }

    @Override
    public MessageDto updateReview(UpdateReviewRequestDto updateReviewRequestDto) {
        BackUpdateReviewRequestDto backUpdateReviewRequestDto = (BackUpdateReviewRequestDto) changeMultipartFileToByte(
            updateReviewRequestDto.validFiles(), updateReviewRequestDto);
        ResponseEntity<MessageDto> response = reviewAdapter.updateReview(
            backUpdateReviewRequestDto);

        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            return response.getBody();
        }
        throw new ReviewUpdateFailedException("review update failed");

    }
}
