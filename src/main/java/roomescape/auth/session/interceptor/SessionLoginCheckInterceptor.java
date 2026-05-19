package roomescape.auth.session.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.session.AuthSessionKey;

@Component
public class SessionLoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AuthSessionKey.LOGIN_MEMBER_ID) == null) {
            throw new UnauthorizedException();
        }

        return true;
    }
}
