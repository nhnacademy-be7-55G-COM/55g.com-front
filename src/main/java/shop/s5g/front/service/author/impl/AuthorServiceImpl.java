package shop.s5g.front.service.author.impl;

import feign.FeignException;
import java.util.List;
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

    //작가 id로 작가 조회
    @Override
    public AuthorResponseDto getAuthorById(long authorId) {
        try {
            ResponseEntity<AuthorResponseDto> author = authorAdapter.findAuthor(authorId);
            return author.getBody();
        }catch (BadRequestException e) {
            throw new AuthorResourceNotFoundException(e.getMessage());
        }
    }

    //작가 수정
    @Override
    public MessageDto updateAuthor(long authorId, AuthorRequestDto authorRequestDto) {
        try {
            ResponseEntity<MessageDto> response = authorAdapter.updateAuthor(authorId, authorRequestDto);
            return response.getBody();
        }catch (BadRequestException e) {
            throw new AuthorResourceNotFoundException(e.getMessage());
        }
    }

    //작가 삭제(비활성화)
    @Override
    public MessageDto deleteAuthor(long authorId) {
        try {
            ResponseEntity<MessageDto> response =  authorAdapter.deleteAuthor(authorId);
            return response.getBody();
        }catch (BadRequestException e) {
            throw new AuthorResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<AuthorResponseDto>> searchAuthors(String keyword){
        try{
            return authorAdapter.searchAuthors(keyword);
        }catch(FeignException e){
            if(e.status()==400){
                throw new BadRequestException();
            }
            throw new RuntimeException();
        }
    }
}
