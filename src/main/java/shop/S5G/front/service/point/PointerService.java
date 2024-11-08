package shop.S5G.front.service.point;

import org.springframework.data.domain.Pageable;
import shop.S5G.front.dto.PageResponseDto;
import shop.S5G.front.dto.point.PointHistoryResponseDto;

public interface PointerService {

    PageResponseDto<PointHistoryResponseDto> getHistory(Pageable pageable);
}
