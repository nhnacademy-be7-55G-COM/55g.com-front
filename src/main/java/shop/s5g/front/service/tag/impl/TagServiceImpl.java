package shop.s5g.front.service.tag.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.TagAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.tag.TagRequestDto;
import shop.s5g.front.dto.tag.TagResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.tag.TagRegisterFailedException;
import shop.s5g.front.service.tag.TagService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    public final TagAdapter tagAdapter;
    @Override
    public MessageDto addTag(TagRequestDto tagRequestDto) {
        try {
            ResponseEntity<MessageDto> response = tagAdapter.addTag(tagRequestDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new TagRegisterFailedException("태그 등록에 실패했습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new TagRegisterFailedException(e.getMessage());
        }
    }

    @Override
    public PageResponseDto<TagResponseDto> getAllTag(Pageable pageable) {
        try {
            ResponseEntity<PageResponseDto<TagResponseDto>> tagResponseDtos = tagAdapter.gatAllTags(pageable);
            return tagResponseDtos.getBody();
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public MessageDto delete(Long tagId) {
        try {
            ResponseEntity<MessageDto> response = tagAdapter.deleteTag(tagId);
            return response.getBody();
        }
        catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<TagResponseDto>> searchTags(String keyword){
        try{
            return tagAdapter.searchTags(keyword);
        }catch(FeignException e){
            if(e.status()==400){
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}