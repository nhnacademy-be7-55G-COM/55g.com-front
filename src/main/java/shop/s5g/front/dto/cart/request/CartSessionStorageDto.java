package shop.s5g.front.dto.cart.request;

import java.util.List;

public record CartSessionStorageDto (
    List<CartBookInfoRequestDto> cartBookInfoList
){}
