package shop.s5g.front.service.point.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.point.PointSourceAdapter;
import shop.s5g.front.dto.point.PointPolicyFormResponseDto;
import shop.s5g.front.dto.point.PointSourceCreateRequestDto;
import shop.s5g.front.service.point.PointSourceService;

@Service
@RequiredArgsConstructor
public class PointSourceServiceImpl implements PointSourceService {

    private final PointSourceAdapter pointSourceAdapter;

    public List<PointPolicyFormResponseDto> getPointPolicyFormData() {
        ResponseEntity<List<PointPolicyFormResponseDto>> pointPolicyFormData = pointSourceAdapter.getPointPolicyFormData();

        return pointPolicyFormData.getBody();
    }

    public void createPointSource(PointSourceCreateRequestDto pointSourceCreateRequestDto) {

        pointSourceAdapter.createPointSource(pointSourceCreateRequestDto);
    }

}
