package roomescape.reservation.repository.entity;

import java.sql.Date;

public record ReservationEntity(
        Long id,
        Long memberId,
        Long roomEscapeCafeId,
        Date date,
        Long timeId,
        Long themeId
) {
}
