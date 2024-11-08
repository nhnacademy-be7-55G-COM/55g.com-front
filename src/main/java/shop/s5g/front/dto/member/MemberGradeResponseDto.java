package shop.s5g.front.dto.member;

public record MemberGradeResponseDto(
    Long memberGradeId,

    String gradeName,

    int gradeCondition,

    int point
) {

}
