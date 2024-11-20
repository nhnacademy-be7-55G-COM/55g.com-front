package shop.s5g.front.dto.point;

import java.io.Serializable;
import java.math.BigDecimal;

public record PointPolicyView(
    String name,
    BigDecimal value
)implements Serializable {

}
