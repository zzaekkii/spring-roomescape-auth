package roomescape.roomescapecafe.repository;

import roomescape.roomescapecafe.domain.RoomEscapeCafe;

import java.util.List;
import java.util.Optional;

public interface RoomEscapeCafeRepository {

    List<RoomEscapeCafe> findAll();

    Optional<RoomEscapeCafe> findById(Long roomEscapeCafeId);
}
