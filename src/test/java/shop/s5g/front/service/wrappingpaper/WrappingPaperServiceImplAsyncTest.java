package shop.s5g.front.service.wrappingpaper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.s5g.front.adapter.WrappingPaperAdapter;
import shop.s5g.front.config.ComponentBuilderConfig;
import shop.s5g.front.config.TestAsyncConfig;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.wrappingpaper.impl.WrappingPaperServiceImpl;

@Slf4j
@EnableAsync
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {
        TestAsyncConfig.class,
        ComponentBuilderConfig.class,
        WrappingPaperServiceImpl.class
    }
)
class WrappingPaperServiceImplAsyncTest {
    @MockBean
    WrappingPaperAdapter wrappingPaperAdapter;

    @MockBean
    ImageService imageService;

    @Autowired
    @Qualifier("wrappingPaperServiceImpl")
    WrappingPaperService service;

    @Test
    void fetchActivePapersTest() {
        when(wrappingPaperAdapter.fetchActivePapers(any())).thenReturn(List.of());

        CompletableFuture<List<WrappingPaperResponseDto>> future = service.fetchActivePapersAsync();
        assertThat(future.join()).isEmpty();

        verify(wrappingPaperAdapter, times(1)).fetchActivePapers(any());
    }

    @Test
    void fetchPaperAsyncTest() {
        WrappingPaperResponseDto resp = mock(WrappingPaperResponseDto.class);
        when(wrappingPaperAdapter.fetchPaper(anyLong())).thenReturn(resp);

        CompletableFuture<WrappingPaperResponseDto> future = service.fetchPaperAsync(1L);
        assertThat(future.join()).isSameAs(resp);

        verify(wrappingPaperAdapter, times(1)).fetchPaper(1L);
    }
}
