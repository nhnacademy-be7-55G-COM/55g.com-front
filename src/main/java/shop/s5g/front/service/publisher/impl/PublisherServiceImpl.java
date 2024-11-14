package shop.s5g.front.service.publisher.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.PublisherAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;
import shop.s5g.front.exception.publisher.PublisherGetFailedException;
import shop.s5g.front.exception.publisher.PublisherRegisterFailedException;
import shop.s5g.front.service.publisher.PublisherService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    public final PublisherAdapter publisherAdapter;

    @Override
    public MessageDto addPublisher(PublisherRequestDto publisherRequestDto) {

        try {
            ResponseEntity<MessageDto> response = publisherAdapter.addPublisher(publisherRequestDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherRegisterFailedException("출판사 등록에 실패했습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherRegisterFailedException(e.getMessage());
        }
    }

    @Override
    public List<PublisherResponseDto> getPublisherList() {
        try {
            ResponseEntity<List<PublisherResponseDto>> allPublisher = publisherAdapter.getAllPublisher();
            if (allPublisher.getStatusCode().is2xxSuccessful()) {
                return allPublisher.getBody();
            }
            throw new PublisherGetFailedException("출판사 조회에 실패했습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }

    //id로 publisher 객체 조회
    @Override
    public PublisherResponseDto getPublisherById(Long id) {
        try {
            ResponseEntity<PublisherResponseDto> publisher = publisherAdapter.findPublisher(id);
            if (publisher.getStatusCode().is2xxSuccessful()) {
                return publisher.getBody();
            }
            throw new PublisherGetFailedException("출판사 조회에 실패했습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
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
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherGetFailedException(e.getMessage());
        }
    }
}
