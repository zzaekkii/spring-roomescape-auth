package roomescape.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.exception.MemberAlreadyExistsException;
import roomescape.member.repository.MemberRepository;
import roomescape.member.service.dto.request.MemberCreateRequest;
import roomescape.member.service.dto.response.MemberResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse create(final MemberCreateRequest request) {
        final Member member = Member.create(
                request.name(),
                request.email(),
                request.password()
        );

        try {
            final Member savedMember = memberRepository.save(member);
            return MemberResponse.from(savedMember);
        } catch (DuplicateKeyException exception) {
            throw new MemberAlreadyExistsException(exception);
        }
    }
}
