package roomescape.member.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.repository.MemberRepository;
import roomescape.member.repository.entity.MemberEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Member> findById(final Long memberId) {
        final String sql = """
                SELECT id, name, email, password, role
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
                SELECT id, name, email, password, role
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

    @Override
    public Member save(final Member member) {
        final MemberEntity memberEntity = toEntity(member);

        final long memberId = insertMember(memberEntity);

        return Member.of(
                memberId,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole()
        );
    }

    private long insertMember(final MemberEntity memberEntity) {
        final String sql = """
                INSERT INTO member (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, memberEntity.name());
            preparedStatement.setString(2, memberEntity.email());
            preparedStatement.setString(3, memberEntity.password());
            preparedStatement.setString(4, memberEntity.role());

            return preparedStatement;
        }, keyHolder);

        return generatedIdFrom(keyHolder);
    }

    private long generatedIdFrom(final KeyHolder keyHolder) {
        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("생성된 id를 가져오지 못했습니다.");
        }

        return keyHolder.getKey().longValue();
    }

    private Member mapToDomain(final ResultSet resultSet, final int rowNum) throws SQLException {
        final MemberEntity memberEntity = mapToEntity(resultSet);

        return Member.of(
                memberEntity.id(),
                memberEntity.name(),
                memberEntity.email(),
                memberEntity.password(),
                MemberRole.valueOf(memberEntity.role())
        );
    }

    private MemberEntity mapToEntity(final ResultSet resultSet) throws SQLException {
        return new MemberEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("role")
        );
    }

    private MemberEntity toEntity(final Member member) {
        return new MemberEntity(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getRole().name()
        );
    }
}
