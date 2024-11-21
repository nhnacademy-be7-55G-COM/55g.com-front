package shop.s5g.front.service.publisher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.publisher.PublisherRequestDto;
import shop.s5g.front.dto.publisher.PublisherResponseDto;


public interface PublisherService {
    MessageDto addPublisher(PublisherRequestDto publisherRequestDto);
    PageResponseDto<PublisherResponseDto> getPublisherList(Pageable pageable);

    PublisherResponseDto getPublisherById(Long id);

    MessageDto updatePublisher(Long id, PublisherRequestDto publisherRequestDto);

    MessageDto delete(Long id);
}
