package shop.s5g.front.service.tag;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.tag.TagRequestDto;
import shop.s5g.front.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {
    MessageDto addTag(TagRequestDto tagRequestDto);

    PageResponseDto<TagResponseDto> getAllTag(Pageable pageable);

    MessageDto delete(Long tagId);

    ResponseEntity<List<TagResponseDto>> searchTags(String keyword);
}
