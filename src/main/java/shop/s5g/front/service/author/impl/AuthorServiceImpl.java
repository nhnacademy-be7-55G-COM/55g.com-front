package shop.s5g.front.service.author.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.front.adapter.AuthorAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.author.AuthorRequestDto;
import shop.s5g.front.dto.author.AuthorResponseDto;
import shop.s5g.front.exception.BadRequestException;
import shop.s5g.front.exception.author.AuthorResourceNotFoundException;
import shop.s5g.front.service.author.AuthorService;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorAdapter authorAdapter;

    //작가 등록
    @Override
    public MessageDto addAuthor(AuthorRequestDto authorRequestDto) {
        try {
            ResponseEntity<MessageDto> response = authorAdapter.addAuthor(authorRequestDto);
            return response.getBody();
        } catch (BadRequestException e) {
            throw new AuthorResourceNotFoundException(e.getMessage());
        }
    }

    //모든 작가 조회
    @Override
    public PageResponseDto<AuthorResponseDto> getAllAuthor(Pageable pageable) {
        try {
            ResponseEntity<PageResponseDto<AuthorResponseDto>> allAuthors = authorAdapter.getAllAuthors(pageable);
            return allAuthors.getBody();
        } catch (BadRequestException e) {
            throw new AuthorResourceNotFoundException(e.getMessage());
        }
    }
}
