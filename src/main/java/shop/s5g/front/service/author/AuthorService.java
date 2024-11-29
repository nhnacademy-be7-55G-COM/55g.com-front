package shop.s5g.front.service.author;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.author.AuthorRequestDto;
import shop.s5g.front.dto.author.AuthorResponseDto;

public interface AuthorService {
    MessageDto addAuthor(AuthorRequestDto authorRequestDto);

    PageResponseDto<AuthorResponseDto> getAllAuthor(Pageable pageable);

    AuthorResponseDto getAuthorById(long authorId);

    MessageDto updateAuthor(long authorId, AuthorRequestDto authorRequestDto);

    MessageDto deleteAuthor(long authorId);

    ResponseEntity<List<AuthorResponseDto>> searchAuthors(String keyword);
}
