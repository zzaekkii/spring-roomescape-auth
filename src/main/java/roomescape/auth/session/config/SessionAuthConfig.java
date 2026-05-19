package roomescape.auth.session.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.auth.session.interceptor.SessionLoginCheckInterceptor;

@Configuration
@RequiredArgsConstructor
public class SessionAuthConfig implements WebMvcConfigurer {

    private final SessionLoginCheckInterceptor sessionLoginCheckInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(sessionLoginCheckInterceptor)
                .addPathPatterns("/reservations", "/reservations/**")
                .excludePathPatterns(
                        "/reservations/available-times",
                        "/reservations/date-and-theme"
                );
    }
}
