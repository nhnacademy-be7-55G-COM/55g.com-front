package shop.s5g.front.service.category.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import shop.s5g.front.adapter.CategoryAdapter;
import shop.s5g.front.dto.MessageDto;
import shop.s5g.front.dto.category.CategoryRequestDto;
import shop.s5g.front.dto.category.CategoryResponseDto;
import shop.s5g.front.exception.category.CategoryRegisterFailedException;
import shop.s5g.front.service.category.CategoryService;

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
//    @Override
//    public List<CategoryResponseDto> getParentCategories() {
//        try {
//            ResponseEntity<List<CategoryResponseDto>> allCategories = categoryAdapter.getAllCategories();
//            if (allCategories.getStatusCode().is2xxSuccessful()) {
//                return allCategories.getBody();
//            }
//            throw new CategoryRegisterFailedException("카테고리가 존재하지 않습니다.");
//        }
//        catch (HttpClientErrorException | HttpServerErrorException e) {
//            throw new CategoryRegisterFailedException(e.getMessage());
//        }
//    }

    /**
     * 국내도서 하위 카테고리 조회
     */
    @Override
    public List<CategoryResponseDto> getKoreaCategories() {
        try{
            ResponseEntity<List<CategoryResponseDto>> koreaCategories = categoryAdapter.getKoreaCategories();
            if (koreaCategories.getStatusCode().is2xxSuccessful()) {
                return koreaCategories.getBody();
            }
            throw new CategoryRegisterFailedException("카테고리가 존재하지 않습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CategoryRegisterFailedException(e.getMessage());
        }
    }

    /**
     * 하위 카테고리 조회
     */
    @Override
    public List<CategoryResponseDto> getChildCategories(long categoryId) {
        try{
            ResponseEntity<List<CategoryResponseDto>> childCategories = categoryAdapter.getChildCategories(categoryId);
            if (childCategories.getStatusCode().is2xxSuccessful()) {
                return childCategories.getBody();
            }
            throw new CategoryRegisterFailedException("카테고리가 존재하지 않습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CategoryRegisterFailedException(e.getMessage());
        }
    }
}