package shop.s5g.front.service.wrappingpaper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.front.adapter.WrappingPaperAdapter;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.wrappingpaper.impl.WrappingPaperServiceImpl;

@ExtendWith(MockitoExtension.class)
class WrappingPaperServiceImplTest {
    @Mock
    WrappingPaperAdapter wrappingPaperAdapter;
    @Mock
    ImageService imageService;

    @InjectMocks
    WrappingPaperServiceImpl service;

    @Test
    void fetchActivePapersTest() {
        when(wrappingPaperAdapter.fetchActivePapers(any())).thenReturn(List.of());

        assertThat(service.fetchActivePapers()).isEmpty();

        verify(wrappingPaperAdapter, times(1)).fetchActivePapers(null);
    }
}
