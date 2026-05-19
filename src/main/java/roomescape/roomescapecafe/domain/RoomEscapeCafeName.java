package roomescape.roomescapecafe.domain;

public class RoomEscapeCafeName {

    private final String name;

    private RoomEscapeCafeName(final String value) {
        validate(value);
        this.name = value;
    }

    public static RoomEscapeCafeName from(final String value) {
        return new RoomEscapeCafeName(value);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("방탈출 카페 이름을 입력해야 합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
