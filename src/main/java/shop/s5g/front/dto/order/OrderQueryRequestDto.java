package shop.s5g.front.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record OrderQueryRequestDto(
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate
) {

}
