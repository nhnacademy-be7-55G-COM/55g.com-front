package shop.s5g.front.service.publisher;

import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;

import java.util.List;

public interface PublisherService {
    MessageDto addPublisher(PublisherRequestDto publisherRequestDto);
    List<PublisherResponseDto> getPublisherList();

    PublisherResponseDto getPublisherById(Long id);

    MessageDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto);
}
