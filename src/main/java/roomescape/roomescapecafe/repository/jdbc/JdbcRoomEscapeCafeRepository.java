package roomescape.roomescapecafe.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.roomescapecafe.domain.RoomEscapeCafe;
import roomescape.roomescapecafe.repository.RoomEscapeCafeRepository;
import roomescape.roomescapecafe.repository.entity.RoomEscapeCafeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcRoomEscapeCafeRepository implements RoomEscapeCafeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RoomEscapeCafe> findAll() {
        final String sql = """
                SELECT id, name
                FROM room_escape_cafe
                ORDER BY id
                """;

        return jdbcTemplate.query(sql, this::mapToDomain)
                .stream()
                .toList();
    }

    @Override
    public Optional<RoomEscapeCafe> findById(final Long roomEscapeCafeId) {
        final String sql = """
                SELECT id, name
                FROM room_escape_cafe
                WHERE id = ?
                """;

        try {
            final RoomEscapeCafe roomEscapeCafe = jdbcTemplate.queryForObject(
                    sql,
                    this::mapToDomain,
                    roomEscapeCafeId
            );

            return Optional.of(roomEscapeCafe);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private RoomEscapeCafe mapToDomain(final ResultSet resultSet, final int rowNum) throws SQLException {
        final RoomEscapeCafeEntity roomEscapeCafeEntity = new RoomEscapeCafeEntity(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );

        return RoomEscapeCafe.of(
                roomEscapeCafeEntity.id(),
                roomEscapeCafeEntity.name()
        );
    }
}
