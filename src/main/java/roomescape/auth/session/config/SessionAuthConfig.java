package roomescape.auth.session.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.session.interceptor.SessionAdminCheckInterceptor;
import roomescape.auth.session.interceptor.SessionLoginCheckInterceptor;
import roomescape.auth.session.resolver.SessionLoginMemberArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SessionAuthConfig implements WebMvcConfigurer {

    private final SessionLoginCheckInterceptor sessionLoginCheckInterceptor;
    private final SessionAdminCheckInterceptor sessionAdminCheckInterceptor;
    private final SessionLoginMemberArgumentResolver sessionLoginMemberArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(sessionLoginCheckInterceptor)
                .addPathPatterns("/reservations", "/reservations/**")
                .excludePathPatterns(
                        "/reservations/available-times",
                        "/reservations/date-and-theme"
                );

        registry.addInterceptor(sessionAdminCheckInterceptor)
                .addPathPatterns(
                        "/admin",
                        "/admin/**",
                        "/times",
                        "/times/**",
                        "/themes",
                        "/themes/**"
                )
                .excludePathPatterns("/themes/popular");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionLoginMemberArgumentResolver);
    }
}
