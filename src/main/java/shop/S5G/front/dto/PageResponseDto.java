package shop.S5G.front.dto;

import java.util.List;

// 기존 Page<T>를 축약하여 페이지 컨텐츠와 페이지 총 개수, 페이지 사이즈, 총 element 개수를 보내는 dto
public record PageResponseDto<T>(List<T> content, int totalPage, int pageSize, long totalElements) {
}
