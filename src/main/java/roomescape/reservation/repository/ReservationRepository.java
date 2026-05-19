package roomescape.reservation.repository;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.dto.ReservationTimesWithStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long reservationId);

    Reservation save(Reservation newReservation);

    boolean update(Reservation updatedReservation);

    boolean deleteById(Long reservationId);

    List<ReservationTimesWithStatus> findReservationTimeStatusesByDateAndThemeIdAndRoomEscapeCafeId(LocalDate date, Long themeId, Long roomEscapeCafeId);

    List<Reservation> findAllByMemberId(Long memberId);

    List<Reservation> findAllByRoomEscapeCafeId(Long roomEscapeCafeId);
}
