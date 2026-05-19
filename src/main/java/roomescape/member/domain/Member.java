package roomescape.member.domain;

import lombok.Getter;

@Getter
public class Member {

    private final Long id;
    private final MemberName name;
    private final String email;
    private final String password;

    private Member(final Long id, final String name, final String email, final String password) {
        validateEmail(email);
        validatePassword(password);

        this.id = id;
        this.name = MemberName.from(name);
        this.email = email;
        this.password = password;
    }

    public static Member of(final Long id, final String name, final String email, final String password) {
        return new Member(id, name, email, password);
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

    public String getName() {
        return name.getName();
    }
}
