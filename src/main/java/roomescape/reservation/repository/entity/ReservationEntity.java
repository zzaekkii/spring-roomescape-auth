package roomescape.reservation.repository.entity;

import java.sql.Date;

public record ReservationEntity(
        Long id,
        Long memberId,
        Date date,
        Long timeId,
        Long themeId
) {
}
