package shop.s5g.front.service.point;

import java.util.List;
import shop.s5g.front.dto.point.PointPolicyFormResponseDto;
import shop.s5g.front.dto.point.PointSourceCreateRequestDto;

public interface PointSourceService {
    void createPointSource(PointSourceCreateRequestDto pointSourceCreateRequestDto);

    List<PointPolicyFormResponseDto> getPointPolicyFormData();
}
