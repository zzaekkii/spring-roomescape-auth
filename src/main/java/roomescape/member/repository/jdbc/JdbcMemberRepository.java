package roomescape.member.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.member.repository.entity.MemberEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Member> findById(final Long memberId) {
        final String sql = """
                SELECT id, name, email, password
                FROM member
                WHERE id = ?
                """;

        try {
            final Member member = jdbcTemplate.queryForObject(
                    sql,
                    this::mapToDomain,
                    memberId
            );

            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        final String sql = """
                SELECT id, name, email, password
                FROM member
                WHERE email = ?
                """;

        try {
            final Member member = jdbcTemplate.queryForObject(
                    sql,
                    this::mapToDomain,
                    email
            );

            return Optional.of(member);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    private Member mapToDomain(final ResultSet resultSet, final int rowNum) throws SQLException {
        final MemberEntity memberEntity = mapToEntity(resultSet);

        return Member.of(
                memberEntity.id(),
                memberEntity.name(),
                memberEntity.email(),
                memberEntity.password()
        );
    }

    private MemberEntity mapToEntity(final ResultSet resultSet) throws SQLException {
        return new MemberEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password")
        );
    }
}
