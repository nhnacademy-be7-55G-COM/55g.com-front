package shop.s5g.front.dto.member;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import shop.s5g.front.dto.address.AddressResponseDto;

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
    Long point,
    List<AddressResponseDto> addresses
) implements Serializable {

}

