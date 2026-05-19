package roomescape.reservation.domain;

import lombok.Getter;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.exception.ReservationCancellationException;
import roomescape.reservation.domain.exception.ReservationModificationException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final Member member, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateRequiredValues(member, date, time);

        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(
            final Member member,
            final LocalDate date,
            final ReservationTime reservationTime,
            final Theme theme,
            final LocalDateTime now
    ) {
        final Reservation reservation = new Reservation(
                null,
                member,
                date,
                reservationTime,
                theme
        );

        reservation.validateNotPast(now);
        return reservation;
    }

    public static Reservation of(
            final Long id,
            final Member member,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme) {
        return new Reservation(
                id,
                member,
                date,
                time,
                theme
        );
    }

    public Reservation changeSchedule(
            final LocalDate date,
            final ReservationTime time,
            final LocalDateTime now
    ) {
        final Reservation changed = new Reservation(
                id,
                member,
                date,
                time,
                theme
        );

        changed.validateNotPast(now);
        return changed;
    }

    public String getMemberName() {
        return member.getName();
    }

    public boolean isOwnedBy(final Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public void validateCancelableByCustomer(final LocalDate today) {
        if (!isBeforeReservationDate(today)) {
            throw new ReservationCancellationException();
        }
    }

    public void validateModifiableByCustomer(final LocalDate today) {
        if (!isBeforeReservationDate(today)) {
            throw new ReservationModificationException();
        }
    }

    private boolean isBeforeReservationDate(final LocalDate today) {
        return today.isBefore(date);
    }

    private void validateRequiredValues(final Member member, final LocalDate date, final ReservationTime time) {
        if (member == null) {
            throw new IllegalArgumentException("예약자를 입력해야 합니다.");
        }

        if (date == null) {
            throw new IllegalArgumentException("예약일을 입력해야 합니다.");
        }

        if (time == null) {
            throw new IllegalArgumentException("예약 시간을 입력해야 합니다.");
        }
    }

    private void validateNotPast(final LocalDateTime now) {
        if (isPast(now)) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    private boolean isPast(final LocalDateTime now) {
        return reservationDateTime().isBefore(now);
    }

    private LocalDateTime reservationDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
