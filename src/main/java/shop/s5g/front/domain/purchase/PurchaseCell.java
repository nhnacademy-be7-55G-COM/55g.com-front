package shop.s5g.front.domain.purchase;

import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;

@RequiredArgsConstructor
@ToString
public class PurchaseCell implements Serializable {
    final BookPurchaseView book;
    // TODO: 쿠폰!
    @Nullable
    Long couponId;
    @Nullable
    WrappingPaperView wrappingPaper;
}
