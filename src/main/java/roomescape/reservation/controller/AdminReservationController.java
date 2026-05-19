package roomescape.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.request.ReservationUpdateRequest;
import roomescape.reservation.service.dto.response.ReservationResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations(
            @RequestParam(value = "roomEscapeCafeId", required = false) Long roomEscapeCafeId
    ) {
        final List<ReservationResponse> results = getReservations(roomEscapeCafeId);
        return ResponseEntity.ok(results);
    }

    private List<ReservationResponse> getReservations(final Long roomEscapeCafeId) {
        if (roomEscapeCafeId == null) {
            return reservationService.getAllReservations();
        }

        return reservationService.getAllReservationsByRoomEscapeCafe(roomEscapeCafeId);
    }

    @PutMapping("/{reservation-id}")
    public ResponseEntity<ReservationResponse> update(
            @PathVariable("reservation-id") Long reservationId,
            @Valid @RequestBody ReservationUpdateRequest request
    ) {
        final ReservationResponse result = reservationService.updateByAdmin(reservationId, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("reservation-id") Long reservationId
    ) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}
