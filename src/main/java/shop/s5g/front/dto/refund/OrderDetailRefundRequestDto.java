package shop.s5g.front.dto.refund;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record OrderDetailRefundRequestDto(
    @Min(1)
    long orderDetailId,
    @NotNull
    @Size(min=1)
    String content,
    @Nullable
    List<MultipartFile> refundImages,
    @Min(1)
    long type
) {

}
