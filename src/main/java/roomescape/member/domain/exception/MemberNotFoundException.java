package roomescape.member.domain.exception;

import roomescape.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }
}
