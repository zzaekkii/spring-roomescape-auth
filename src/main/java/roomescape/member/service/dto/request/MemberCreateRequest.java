package roomescape.member.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberCreateRequest(
        @NotBlank(message = "이름을 입력해야 합니다.")
        String name,

        @NotBlank(message = "이메일을 입력해야 합니다.")
        String email,

        @NotBlank(message = "비밀번호를 입력해야 합니다.")
        String password
) {
}
