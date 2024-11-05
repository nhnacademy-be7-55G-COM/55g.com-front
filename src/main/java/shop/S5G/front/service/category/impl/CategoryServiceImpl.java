package shop.S5G.front.service.category.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.S5G.front.adapter.CategoryAdapter;
import shop.S5G.front.dto.MessageDto;
import shop.S5G.front.dto.category.CategoryRequestDto;
import shop.S5G.front.dto.category.CategoryResponseDto;
import shop.S5G.front.exception.category.CategoryRegisterFailedException;
import shop.S5G.front.service.category.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryAdapter categoryAdapter;

    /**
     * 카테고리 등록
     */
    @Override
    public MessageDto addCategory(CategoryRequestDto requestDto) {
        try{
            ResponseEntity<MessageDto> response = categoryAdapter.addsCategory(requestDto);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CategoryRegisterFailedException("카테고리 등록 실패");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CategoryRegisterFailedException(e.getMessage());
        }
    }

    /**
     * 모든 카테고리 조회
     */
    @Override
    public List<CategoryResponseDto> getParentCategories() {
        try {
            ResponseEntity<List<CategoryResponseDto>> allCategories = categoryAdapter.getAllCategories();
            if (allCategories.getStatusCode().is2xxSuccessful()) {
                return allCategories.getBody();
            }
            throw new CategoryRegisterFailedException("카테고리가 존재하지 않습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CategoryRegisterFailedException(e.getMessage());
        }
    }
}