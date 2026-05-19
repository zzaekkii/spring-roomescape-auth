package roomescape.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public Member login(final LoginRequest request) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!member.hasPassword(request.password())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return member;
    }
}
