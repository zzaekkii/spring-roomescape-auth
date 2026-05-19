package roomescape.auth.session.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.annotation.LoginMember;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.session.AuthSessionKey;

@Component
public class SessionLoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new UnauthorizedException();
        }

        return getLoginMemberId(request);
    }

    private Long getLoginMemberId(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session == null) {
            throw new UnauthorizedException();
        }

        final Object loginMemberId = session.getAttribute(AuthSessionKey.LOGIN_MEMBER_ID);

        if (!(loginMemberId instanceof Long memberId)) {
            throw new UnauthorizedException();
        }

        return memberId;
    }
}
