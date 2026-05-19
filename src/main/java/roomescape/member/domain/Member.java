package roomescape.member.domain;

import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final MemberName name;
    private final String email;
    private final String password;
    private final MemberRole role;

    private Member(final Long id, final String name, final String email, final String password, final MemberRole role) {
        validateEmail(email);
        validatePassword(password);
        validateRole(role);

        this.id = id;
        this.name = MemberName.from(name);
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member create(final String name, final String email, final String password) {
        return new Member(null, name, email, password, MemberRole.USER);
    }

    public static Member of(
            final Long id,
            final String name,
            final String email,
            final String password,
            final MemberRole role
    ) {
        return new Member(id, name, email, password, role);
    }

    public boolean hasPassword(final String password) {
        return this.password.equals(password);
    }

    private void validateEmail(final String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해야 합니다.");
        }
    }

    private void validatePassword(final String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
        }
    }

    private void validateRole(final MemberRole role) {
        if (role == null) {
            throw new IllegalArgumentException("회원 권한을 입력해야 합니다.");
        }
    }

    public String getName() {
        return name.getName();
    }
}
