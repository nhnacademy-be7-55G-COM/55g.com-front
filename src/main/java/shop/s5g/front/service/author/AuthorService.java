package shop.s5g.front.service.author;

import org.springframework.data.domain.Pageable;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.PageResponseDto;
import shop.s5g.front.dto.author.AuthorRequestDto;
import shop.s5g.front.dto.author.AuthorResponseDto;

public interface AuthorService {
    MessageDto addAuthor(AuthorRequestDto authorRequestDto);

    PageResponseDto<AuthorResponseDto> getAllAuthor(Pageable pageable);
}
