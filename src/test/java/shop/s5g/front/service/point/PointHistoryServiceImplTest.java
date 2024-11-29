package shop.s5g.front.service.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.adapter.point.PointAdapter;
import shop.s5g.front.service.point.impl.PointHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceImplTest {
    @Mock
    PointAdapter pointAdapter;

    @InjectMocks
    PointHistoryServiceImpl service;

    @Test
    void getHistoryTest() {
        assertThatCode(() -> service.getHistory(Pageable.ofSize(10)))
            .doesNotThrowAnyException();

        verify(pointAdapter, times(1)).fetchPointHistory(anyInt(), anyInt());
    }

}
