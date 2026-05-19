package roomescape.reservation.service.dto.response;

import roomescape.theme.service.dto.response.ThemeResponse;
import roomescape.roomescapecafe.service.dto.response.RoomEscapeCafeResponse;

import java.time.LocalDate;
import java.util.List;

public record ReservationOptionResponse(
        List<LocalDate> dates,
        List<ThemeResponse> themes,
        List<RoomEscapeCafeResponse> roomEscapeCafes
) {
}
