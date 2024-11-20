package shop.s5g.front.dto.member;

import java.io.Serializable;

public record MemberStatusResponseDto(
    Long memberStatusId,
    String typeName
) implements Serializable {

}
