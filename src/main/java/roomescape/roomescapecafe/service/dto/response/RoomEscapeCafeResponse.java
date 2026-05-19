package roomescape.roomescapecafe.service.dto.response;

import roomescape.roomescapecafe.domain.RoomEscapeCafe;

public record RoomEscapeCafeResponse(
        Long id,
        String name
) {

    public static RoomEscapeCafeResponse from(final RoomEscapeCafe roomEscapeCafe) {
        return new RoomEscapeCafeResponse(
                roomEscapeCafe.getId(),
                roomEscapeCafe.getName()
        );
    }
}
