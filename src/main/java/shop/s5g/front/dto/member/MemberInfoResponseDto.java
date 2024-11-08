package shop.s5g.front.dto.member;

import java.time.LocalDateTime;

public record MemberInfoResponseDto(
    Long customerId,
    MemberStatusResponseDto status,
    MemberGradeResponseDto grade,
    String loginId,
    String password,
    String birth,
    String name,
    String email,
    String phoneNumber,
    LocalDateTime createdAt,
    LocalDateTime latestLoginAt,
    Long point
) {

}

