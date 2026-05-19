package roomescape.reservation.service.dto.response;

import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.service.dto.response.ReservationTimeResponse;
import roomescape.roomescapecafe.service.dto.response.RoomEscapeCafeResponse;
import roomescape.theme.service.dto.response.ThemeResponse;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        RoomEscapeCafeResponse roomEscapeCafe
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMemberName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                RoomEscapeCafeResponse.from(reservation.getRoomEscapeCafe())
        );
    }
}
