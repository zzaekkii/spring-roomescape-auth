package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.annotation.LoginMember;
import roomescape.reservation.repository.dto.ReservationTimesWithStatus;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.request.ReservationCreateRequest;
import roomescape.reservation.service.dto.request.ReservationUpdateRequest;
import roomescape.reservation.service.dto.response.ReservationOptionResponse;
import roomescape.reservation.service.dto.response.ReservationResponse;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @LoginMember Long memberId
    ) {
        final List<ReservationResponse> results = reservationService.getReservationsByMemberId(memberId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<ReservationTimesWithStatus>> getReservationTimeStatuses(
            @RequestParam(value = "date") LocalDate date,
            @RequestParam(value = "themeId") Long themeId,
            @RequestParam(value = "roomEscapeCafeId") Long roomEscapeCafeId
    ) {
        final List<ReservationTimesWithStatus> results = reservationService.getReservationTimeStatuses(date, themeId, roomEscapeCafeId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/date-and-theme")
    public ResponseEntity<ReservationOptionResponse> getReservationOptions() {
        final ReservationOptionResponse results = reservationService.getReservationOptions();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @LoginMember Long memberId,
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        final ReservationResponse result = reservationService.create(memberId, request);
        return ResponseEntity.created(URI.create("/reservations"))
                .body(result);
    }

    @PutMapping("/{reservation-id}")
    public ResponseEntity<ReservationResponse> update(
            @LoginMember Long memberId,
            @PathVariable("reservation-id") Long reservationId,
            @Valid @RequestBody ReservationUpdateRequest request
    ) {
        final ReservationResponse result = reservationService.updateByCustomer(memberId, reservationId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> cancel(
            @LoginMember Long memberId,
            @PathVariable("reservation-id") Long reservationId
    ) {
        reservationService.cancel(memberId, reservationId);
        return ResponseEntity.noContent().build();
    }
}
