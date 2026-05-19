package roomescape.member.domain;

public class MemberName {

    private final String name;

    private MemberName(final String value) {
        validate(value);
        this.name = value;
    }

    public static MemberName from(final String value) {
        return new MemberName(value);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이름을 입력해야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
