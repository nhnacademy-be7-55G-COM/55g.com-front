package shop.s5g.front.service.point;


import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.adapter.point.PointSourceAdapter;
import shop.s5g.front.dto.point.PointPolicyFormResponseDto;
import shop.s5g.front.dto.point.PointSourceCreateRequestDto;
import shop.s5g.front.service.point.impl.PointSourceServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PointSourceServiceImplTest {

    @Mock
    PointSourceAdapter pointSourceAdapter;

    @InjectMocks
    PointSourceServiceImpl service;

    @Test
    void getPointPolicyFormDataTest() {
        List<PointPolicyFormResponseDto> pointPolicyFormData = new ArrayList<>();
        pointPolicyFormData.add(new PointPolicyFormResponseDto(1l, "sourceName1"));

        when(pointSourceAdapter.getPointPolicyFormData()).thenReturn(
            ResponseEntity.ok().body(pointPolicyFormData));

        List<PointPolicyFormResponseDto> actualResult = service.getPointPolicyFormData();

        Assertions.assertEquals(pointPolicyFormData, actualResult);
    }

    @Test
    void createPointSourceTest() {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            "sourceName1");

        when(pointSourceAdapter.createPointSource(pointSourceCreateRequestDto)).thenReturn(
            ResponseEntity.ok().build());

        assertThatCode(() -> service.createPointSource(
            pointSourceCreateRequestDto)).doesNotThrowAnyException();

    }
}
