package roomescape.reservation.service.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationCreateRequest(
        @NotNull(message = "방탈출 카페를 선택해야 합니다.")
        Long roomEscapeCafeId,
        @NotNull(message = "예약일을 입력해야 합니다.")
        LocalDate date,
        @NotNull(message = "예약 시간을 선택해야 합니다.")
        Long timeId,
        @NotNull(message = "테마를 선택해야 합니다.")
        Long themeId
) {
}
