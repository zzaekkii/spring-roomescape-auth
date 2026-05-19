package roomescape.member.domain.exception;

import roomescape.common.exception.ConflictException;

public class MemberAlreadyExistsException extends ConflictException {

    public MemberAlreadyExistsException(final Throwable cause) {
        super("이미 사용 중인 이메일입니다.", cause);
    }
}
