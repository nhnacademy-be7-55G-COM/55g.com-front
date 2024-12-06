package shop.s5g.front.service.image;

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
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.ImageProviderAdapter;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.service.image.impl.ImageServiceImpl;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {
    @Mock
    ImageProviderAdapter imageProviderAdapter;

    @InjectMocks
    ImageServiceImpl service;

    MockMultipartFile mockFile = new MockMultipartFile("image.png", "--image-content--".getBytes());
    Map<String, Object> response = new HashMap<>();

    final String testUrl = "http://test-url";
    final long bytes = mockFile.getSize();
    final String path = "/";
    final String id = "test-id";

    @BeforeEach
    void responseInit() {
        Map<String, Object> file = new HashMap<>();
        file.put("url", testUrl);
        file.put("bytes", bytes);
        file.put("path", path);
        file.put("id", id);
        response.put("file", file);
    }

    @Test
    void uploadImageTest() {
        when(imageProviderAdapter.uploadImage(anyString(), any())).thenReturn(response);

        assertThatCode(() -> service.uploadImage(path, mockFile))
            .doesNotThrowAnyException();

        verify(imageProviderAdapter, times(1)).uploadImage(anyString(), any());
    }

    @Test
    void uploadImageExceptionTest() throws IOException {
        MultipartFile mockFileExp = mock(MultipartFile.class);
        when(mockFileExp.getBytes()).thenThrow(IOException.class);

        assertThatThrownBy(() -> service.uploadImage(path, mockFileExp))
            .isInstanceOf(ApplicationException.class);

        verify(imageProviderAdapter, never()).uploadImage(anyString(), any());
    }

}
