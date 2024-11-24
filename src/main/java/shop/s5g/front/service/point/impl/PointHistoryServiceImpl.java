package shop.s5g.front.service.point.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.point.PointAdapter;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.point.PointHistoryResponseDto;
import shop.s5g.front.service.point.PointHistoryService;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointAdapter pointAdapter;

    @Override
    public PageResponseDto<PointHistoryResponseDto> getHistory(Pageable pageable) {
        return pointAdapter.fetchPointHistory(pageable.getPageSize(), pageable.getPageNumber());
    }

}
