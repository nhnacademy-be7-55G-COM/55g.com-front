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
import shop.s5g.front.exception.BookNotFoundException;
import shop.s5g.front.exception.tag.TagBadRequestException;
import shop.s5g.front.exception.tag.TagRegisterFailedException;
import shop.s5g.front.exception.tag.TagResourceNotFoundException;
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
                return response.getBody();
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BookNotFoundException();
            } else if (e.status() == 400) {
                throw new BadRequestException();
            }
        }
        throw new RuntimeException();
    }

    @Override
    public PageResponseDto<TagResponseDto> getAllTag(Pageable pageable) {
        try {
            ResponseEntity<PageResponseDto<TagResponseDto>> tagResponseDtos = tagAdapter.gatAllTags(pageable);
            return tagResponseDtos.getBody();
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new BookNotFoundException();
            } else if (e.status() == 400) {
                throw new BadRequestException();
            }
        }
        throw new RuntimeException();
    }

    @Override
    public MessageDto delete(Long tagId) {
        try {
            ResponseEntity<MessageDto> response = tagAdapter.deleteTag(tagId);
            return response.getBody();
        }
        catch (FeignException e) {
            if (e.status() == 404) {
                throw new TagResourceNotFoundException(e.getMessage());
            } else if (e.status() == 400) {
                throw new TagBadRequestException(e.getMessage());
            }
        }
        throw new RuntimeException();
    }
}