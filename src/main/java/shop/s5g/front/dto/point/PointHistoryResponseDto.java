package shop.s5g.front.dto.point;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record PointHistoryResponseDto(
    long id,    // point history id
    String pointSource,
    long point,
    long remainingPoint,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt
) {

}
