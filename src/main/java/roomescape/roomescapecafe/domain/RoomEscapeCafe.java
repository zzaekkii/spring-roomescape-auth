package roomescape.roomescapecafe.domain;

import lombok.Getter;

@Getter
public class RoomEscapeCafe {

    private final Long id;
    private final RoomEscapeCafeName name;

    private RoomEscapeCafe(final Long id, final String name) {
        this.id = id;
        this.name = RoomEscapeCafeName.from(name);
    }

    public static RoomEscapeCafe of(final Long id, final String name) {
        return new RoomEscapeCafe(id, name);
    }

    public String getName() {
        return name.getName();
    }
}
