package roomescape.member.repository.entity;

public record MemberEntity(
        Long id,
        String name,
        String email,
        String password,
        String role
) {
}
