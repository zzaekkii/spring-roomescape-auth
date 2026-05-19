package roomescape.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.domain.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.roomescapecafe.domain.RoomEscapeCafe;
import roomescape.roomescapecafe.domain.exception.RoomEscapeCafeNotFoundException;
import roomescape.roomescapecafe.repository.RoomEscapeCafeRepository;
import roomescape.roomescapecafe.service.dto.response.RoomEscapeCafeResponse;
import roomescape.theme.domain.Theme;
import roomescape.reservation.domain.exception.ReservationAlreadyExistsException;
import roomescape.reservation.domain.exception.ReservationNotFoundException;
import roomescape.reservation.domain.exception.ReservationOptionChangedException;
import roomescape.reservationtime.domain.exception.ReservationTimeNotFoundException;
import roomescape.theme.domain.exception.ThemeNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.reservation.repository.dto.ReservationTimesWithStatus;
import roomescape.reservation.service.dto.request.ReservationCreateRequest;
import roomescape.reservation.service.dto.request.ReservationUpdateRequest;
import roomescape.reservation.service.dto.response.ReservationOptionResponse;
import roomescape.reservation.service.dto.response.ReservationResponse;
import roomescape.theme.service.dto.response.ThemeResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final int RESERVABLE_DAYS_RANGE = 14;

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final RoomEscapeCafeRepository roomEscapeCafeRepository;
    private final Clock clock;

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getReservationsByMemberId(final Long memberId) {
        return reservationRepository.findAllByMemberId(memberId)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getAllReservationsByRoomEscapeCafe(final Long roomEscapeCafeId) {
        getRoomEscapeCafe(roomEscapeCafeId);

        return reservationRepository.findAllByRoomEscapeCafeId(roomEscapeCafeId)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationTimesWithStatus> getReservationTimeStatuses(final LocalDate date, final Long themeId, final Long roomEscapeCafeId) {
        getRoomEscapeCafe(roomEscapeCafeId);

        return reservationRepository.findReservationTimeStatusesByDateAndThemeIdAndRoomEscapeCafeId(date, themeId, roomEscapeCafeId);
    }

    public ReservationResponse create(final Long memberId, final ReservationCreateRequest data) {
        final Member member = getMember(memberId);
        final ReservationTime reservationTime = getReservationTime(data.timeId());
        final Theme theme = getTheme(data.themeId());
        final RoomEscapeCafe roomEscapeCafe = getRoomEscapeCafe(data.roomEscapeCafeId());

        final Reservation reservation = Reservation.create(
                member,
                data.date(),
                reservationTime,
                theme,
                roomEscapeCafe,
                LocalDateTime.now(clock)
        );

        final Reservation savedReservation = saveReservation(reservation);

        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse updateByCustomer(final Long memberId, final Long reservationId, final ReservationUpdateRequest data) {
        final Reservation originReservation = getReservation(reservationId);
        validateOwner(originReservation, memberId);
        originReservation.validateModifiableByCustomer(LocalDate.now(clock));

        return updateSchedule(data, originReservation);
    }

    public ReservationResponse updateByAdmin(final Long reservationId, final ReservationUpdateRequest data) {
        final Reservation originReservation = getReservation(reservationId);

        return updateSchedule(data, originReservation);
    }

    public void cancel(final Long memberId, final Long reservationId) {
        final Reservation reservation = getReservation(reservationId);

        validateOwner(reservation, memberId);
        reservation.validateCancelableByCustomer(LocalDate.now(clock));

        deleteReservation(reservationId);
    }

    public void delete(final Long reservationId) {
        deleteReservation(reservationId);
    }

    public ReservationOptionResponse getReservationOptions() {
        LocalDate today = LocalDate.now(clock);
        List<LocalDate> dates = today.datesUntil(today.plusDays(RESERVABLE_DAYS_RANGE)).toList();

        List<ThemeResponse> themes = themeRepository.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();

        List<RoomEscapeCafeResponse> roomEscapeCafes = roomEscapeCafeRepository.findAll()
                .stream()
                .map(RoomEscapeCafeResponse::from)
                .toList();

        return new ReservationOptionResponse(dates, themes, roomEscapeCafes);
    }

    private ReservationResponse updateSchedule(final ReservationUpdateRequest data, final Reservation originReservation) {
        final ReservationTime newReservationTime = getReservationTime(data.timeId());

        final Reservation updatedReservation = originReservation.changeSchedule(
                data.date(),
                newReservationTime,
                LocalDateTime.now(clock)
        );
        final Reservation reservation = updateReservation(updatedReservation);

        return ReservationResponse.from(reservation);
    }

    private Reservation saveReservation(final Reservation reservation) {
        try {
            return reservationRepository.save(reservation);
        } catch (DuplicateKeyException exception) {
            throw new ReservationAlreadyExistsException(exception);
        } catch (DataIntegrityViolationException exception) {
            throw new ReservationOptionChangedException(exception);
        }
    }

    private Reservation updateReservation(final Reservation reservation) {
        try {
            final boolean updated = reservationRepository.update(reservation);

            if (!updated) {
                throw new ReservationNotFoundException();
            }

            return reservation;
        } catch (DuplicateKeyException exception) {
            throw new ReservationAlreadyExistsException(exception);
        } catch (DataIntegrityViolationException exception) {
            throw new ReservationOptionChangedException(exception);
        }
    }

    private void deleteReservation(final Long reservationId) {
        final boolean deleted = reservationRepository.deleteById(reservationId);

        if (!deleted) {
            throw new ReservationNotFoundException();
        }
    }

    private Reservation getReservation(final Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private ReservationTime getReservationTime(final Long reservationTimeId) {
        return reservationTimeRepository.findById(reservationTimeId)
                .orElseThrow(ReservationTimeNotFoundException::new);
    }

    private Theme getTheme(final Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(ThemeNotFoundException::new);
    }

    private RoomEscapeCafe getRoomEscapeCafe(final Long roomEscapeCafeId) {
        return roomEscapeCafeRepository.findById(roomEscapeCafeId)
                .orElseThrow(RoomEscapeCafeNotFoundException::new);
    }

    private void validateOwner(final Reservation reservation, final Long memberId) {
        if (!reservation.isOwnedBy(memberId)) {
            throw new UnauthorizedException("본인의 예약만 처리할 수 있습니다.");
        }
    }
}
