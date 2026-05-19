package roomescape.auth.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        this("로그인이 필요합니다.");
    }

    public UnauthorizedException(final String message) {
        super(message);
    }
}
