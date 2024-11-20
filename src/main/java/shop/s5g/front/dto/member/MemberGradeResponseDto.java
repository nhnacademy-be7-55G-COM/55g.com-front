package shop.s5g.front.dto.member;

import java.io.Serializable;

public record MemberGradeResponseDto(
    Long memberGradeId,

    String gradeName,

    int gradeCondition,

    int point
) implements Serializable {

}
