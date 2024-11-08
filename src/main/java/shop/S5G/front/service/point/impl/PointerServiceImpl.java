package shop.S5G.front.service.point.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.S5G.front.adapter.PointAdapter;
import shop.S5G.front.dto.PageResponseDto;
import shop.S5G.front.dto.point.PointHistoryResponseDto;
import shop.S5G.front.service.point.PointerService;

@Service
@RequiredArgsConstructor
public class PointerServiceImpl implements PointerService {
    private final PointAdapter pointAdapter;

    @Override
    public PageResponseDto<PointHistoryResponseDto> getHistory(Pageable pageable) {
        return pointAdapter.fetchPointHistory(pageable.getPageSize(), pageable.getPageNumber());
    }

}
