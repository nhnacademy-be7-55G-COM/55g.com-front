package shop.s5g.front.service.review.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import shop.s5g.front.adapter.review.ReviewAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.review.*;
import shop.s5g.front.exception.review.ReviewRegisterFailedException;
import shop.s5g.front.exception.review.ReviewUpdateFailedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewAdapter reviewAdapter;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private MockMultipartFile mockFile;
    private CreateReviewRequestDto createReviewRequestDto;
    private UpdateReviewRequestDto updateReviewRequestDto;

    @BeforeEach
    void setUp() {
        // MockMultipartFile 준비
        mockFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test data".getBytes());

        // 새로운 DTO 구조에 맞춰 실제 인스턴스 생성
        // CreateReviewRequestDto(
        // @NotNull Long bookId,
        // @NotNull Long orderDetailId,
        // @Min(1) @Max(5) int score,
        // String content,
        // List<MultipartFile> attachment
        // )
        createReviewRequestDto = new CreateReviewRequestDto(
            100L,        // bookId
            200L,        // orderDetailId
            5,           // score
            "Great book!",
            List.of(mockFile)
        );

        // UpdateReviewRequestDto(
        // @NotNull Long reviewId,
        // @Min(1) @Max(5) int score,
        // String content,
        // List<MultipartFile> attachment
        // )
        updateReviewRequestDto = new UpdateReviewRequestDto(
            1L,      // reviewId
            4,       // score
            "Updated review content",
            List.of(mockFile)
        );
    }

    @Test
    void addReview_Success() {
        MessageDto message = new MessageDto("success");
        when(reviewAdapter.registerReview(any(BackCreateReviewRequestDto.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(message));

        MessageDto result = reviewService.addReview(createReviewRequestDto);
        assertNotNull(result);
        assertEquals("success", result.message());

        verify(reviewAdapter, times(1)).registerReview(any(BackCreateReviewRequestDto.class));
    }

    @Test
    void addReview_Failed() {
        // CREATED 가 아닌 경우 실패
        when(reviewAdapter.registerReview(any(BackCreateReviewRequestDto.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        assertThrows(ReviewRegisterFailedException.class, () -> reviewService.addReview(createReviewRequestDto));
        verify(reviewAdapter, times(1)).registerReview(any(BackCreateReviewRequestDto.class));
    }

    @Test
    void getReviewList_Success() {
        // PageResponseDto는 record: (List<T> content, int totalPage, int pageSize, long totalElements)
        PageResponseDto<ReviewResponseDto> pageResponse = new PageResponseDto<>(List.of(), 0, 10, 0L);
        when(reviewAdapter.getReviewList(any())).thenReturn(pageResponse);

        PageResponseDto<ReviewResponseDto> result = reviewService.getReviewList(PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(0, result.content().size());
        assertEquals(0, result.totalPage());
        assertEquals(10, result.pageSize());
        assertEquals(0L, result.totalElements());

        verify(reviewAdapter, times(1)).getReviewList(any());
    }

    @Test
    void updateReview_Success() {
        MessageDto message = new MessageDto("update success");
        when(reviewAdapter.updateReview(any(BackUpdateReviewRequestDto.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(message));

        MessageDto result = reviewService.updateReview(updateReviewRequestDto);
        assertNotNull(result);
        assertEquals("update success", result.message());

        verify(reviewAdapter, times(1)).updateReview(any(BackUpdateReviewRequestDto.class));
    }

    @Test
    void updateReview_Failed() {
        when(reviewAdapter.updateReview(any(BackUpdateReviewRequestDto.class)))
            .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        assertThrows(ReviewUpdateFailedException.class, () -> reviewService.updateReview(updateReviewRequestDto));

        verify(reviewAdapter, times(1)).updateReview(any(BackUpdateReviewRequestDto.class));
    }

}
