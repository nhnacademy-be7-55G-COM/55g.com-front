package shop.S5G.front.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record OrderQueryRequestDto(
    long customerId,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate
) {

}
