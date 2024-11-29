package shop.s5g.front.service.refund;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.refund.RefundAdapter;
import shop.s5g.front.dto.refund.OrderDetailRefundRequestDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.refund.impl.RefundServiceImpl;

@ExtendWith(MockitoExtension.class)
class RefundServiceImplTest {
    @Mock
    RefundAdapter refundAdapter;

    @Mock
    ImageService imageService;

    @InjectMocks
    RefundServiceImpl service;

    List<MultipartFile> fileList = List.of(new MockMultipartFile("image.png", "--image--".getBytes()));

    OrderDetailRefundRequestDto request = new OrderDetailRefundRequestDto(
        1L, "환불하고싶어요", fileList, 1L
    );

    MultipartFile mockFile= mock(MultipartFile.class);

    OrderDetailRefundRequestDto requestFail = new OrderDetailRefundRequestDto(
        1L, "환불하고싶어요", List.of(mockFile), 1L
    );
    @Test
    void registerRefundTest() {
        assertThatCode(() -> service.registerRefund(request))
            .doesNotThrowAnyException();

        verify(imageService, times(fileList.size())).uploadImage(anyString(), any(byte[].class));
        verify(refundAdapter, times(1)).createRefund(any());
    }

    @Test
    void registerRefundFailTest() throws Exception {
        when(mockFile.getBytes()).thenThrow(IOException.class);

        assertThatThrownBy(() -> service.registerRefund(requestFail))
            .isInstanceOf(ApplicationException.class);

        verify(imageService, never()).uploadImage(anyString(), any(byte[].class));
        verify(refundAdapter, never()).createRefund(any());
    }

}
