package shop.s5g.front.service.refund.impl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.s5g.front.adapter.refund.RefundAdapter;
import shop.s5g.front.dto.publisher.RefundCreateRequestDto;
import shop.s5g.front.dto.refund.OrderDetailRefundRequestDto;
import shop.s5g.front.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.front.exception.ApplicationException;
import shop.s5g.front.service.image.ImageService;
import shop.s5g.front.service.refund.RefundService;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final ImageService imageService;
    private final RefundAdapter refundAdapter;

    @Override
    public RefundHistoryCreateResponseDto registerRefund(OrderDetailRefundRequestDto request) {
        List<MultipartFile> images = request.refundImages();
        List<String> imagePathList = new LinkedList<>();
        try {
            if (images != null && !images.isEmpty()) {
                // 이미지 파일 MD5 digest 및 업로드.
                for (MultipartFile image: images) {
                    byte[] imageByte = image.getBytes();
                    String filename = DigestUtils.md5Hex(imageByte);
                    String extension = FilenameUtils.getExtension(image.getOriginalFilename());

                    String digestName = String.format("%s.%s", filename, extension);
                    imageService.uploadImage(digestName, imageByte);
                    imagePathList.add(digestName);
                }
            }

            return refundAdapter.createRefund(
                new RefundCreateRequestDto(request.orderDetailId(), request.content(), request.type(), imagePathList)
            );
        }catch (IOException e) {
            throw new ApplicationException("이미지 업로드 도중 문제가 발생했습니다.");
        }
    }

}
