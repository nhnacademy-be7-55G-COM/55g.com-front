package shop.s5g.front.service.publisher.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.PublisherAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.publisher.PublisherGetFailedException;
import shop.s5g.front.service.publisher.PublisherService;


@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    public final PublisherAdapter publisherAdapter;

    @Override
    public MessageDto addPublisher(PublisherRequestDto publisherRequestDto) {

        try {
            ResponseEntity<MessageDto> response = publisherAdapter.addPublisher(publisherRequestDto);

                return response.getBody();
        } catch (BadRequestException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }

    //출판사 전제 조회


    @Override
    public PageResponseDto<PublisherResponseDto> getPublisherList(Pageable pageable) {
        try {
            ResponseEntity<PageResponseDto<PublisherResponseDto>> allPublisher = publisherAdapter.getAllPublisher(pageable);
                return allPublisher.getBody();
        } catch (BadRequestException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }

    //id로 publisher 객체 조회
    @Override
    public PublisherResponseDto getPublisherById(Long id) {
        try {
            ResponseEntity<PublisherResponseDto> publisher = publisherAdapter.findPublisher(id);
                return publisher.getBody();
        } catch (BadRequestException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }

    //수정
    @Override
    public MessageDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto) {
        try {
            ResponseEntity<MessageDto> updatedPublisher = publisherAdapter.updatePublisher(id, publisherRequestDto);
            if(updatedPublisher.getStatusCode().is2xxSuccessful()) {
                return updatedPublisher.getBody();
            }
            throw new PublisherGetFailedException("출판사 수정에 실패했습니다.");
        }
        catch (BadRequestException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }

    //출판사 삭제(비활성화)
    @Override
    public MessageDto delete(Long id) {
        try{
            ResponseEntity<MessageDto> response = publisherAdapter.deletePublisher(id);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherGetFailedException("해당 출판사는 없습니다.");
        }catch (BadRequestException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }
}
