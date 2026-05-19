package roomescape.auth.session.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.session.AuthSessionKey;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class SessionAdminCheckInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final Member member = getLoginMember(request);

        if (member.getRole() != MemberRole.ADMIN) {
            throw new ForbiddenException();
        }

        return true;
    }

    private Member getLoginMember(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            throw new UnauthorizedException();
        }

        final Object loginMemberId = session.getAttribute(AuthSessionKey.LOGIN_MEMBER_ID);

        if (!(loginMemberId instanceof Long memberId)) {
            throw new UnauthorizedException();
        }

        return memberRepository.findById(memberId)
                .orElseThrow(UnauthorizedException::new);
    }
}
