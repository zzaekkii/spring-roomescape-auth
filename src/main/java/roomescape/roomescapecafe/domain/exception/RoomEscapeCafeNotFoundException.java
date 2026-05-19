package roomescape.roomescapecafe.domain.exception;

import roomescape.common.exception.NotFoundException;

public class RoomEscapeCafeNotFoundException extends NotFoundException {

    public RoomEscapeCafeNotFoundException() {
        super("존재하지 않는 방탈출 카페입니다.");
    }
}
