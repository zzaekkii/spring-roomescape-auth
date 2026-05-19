package roomescape.auth.session.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.auth.session.AuthSessionKey;
import roomescape.member.domain.Member;

@RestController
@RequiredArgsConstructor
public class SessionAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody final LoginRequest request,
            final HttpSession session
    ) {
        final Member member = authService.login(request);
        session.setAttribute(AuthSessionKey.LOGIN_MEMBER_ID, member.getId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }
}
