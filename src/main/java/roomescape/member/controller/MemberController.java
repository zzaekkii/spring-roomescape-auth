package roomescape.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.service.MemberService;
import roomescape.member.service.dto.request.MemberCreateRequest;
import roomescape.member.service.dto.response.MemberResponse;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> create(
            @Valid @RequestBody final MemberCreateRequest request
    ) {
        final MemberResponse response = memberService.create(request);

        return ResponseEntity.created(URI.create("/members/" + response.id()))
                .body(response);
    }
}
