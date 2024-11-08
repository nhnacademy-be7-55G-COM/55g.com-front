package shop.s5g.front.service.point;

import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.point.PointHistoryResponseDto;

public interface PointerService {

    PageResponseDto<PointHistoryResponseDto> getHistory(Pageable pageable);
}
