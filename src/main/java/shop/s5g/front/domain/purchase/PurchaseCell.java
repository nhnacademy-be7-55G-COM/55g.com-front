package shop.s5g.front.domain.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import shop.s5g.front.dto.book.BookPurchaseView;
import shop.s5g.front.dto.wrappingpaper.WrappingPaperView;

@RequiredArgsConstructor
public class PurchaseCell {
    final BookPurchaseView book;
    @Nullable
    Long couponId;
    @Nullable
    WrappingPaperView wrappingPaper;
}
