package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemverV1(@RequestBody @Valid Member member) {

        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);

    }

    @PostMapping("/api/v2/members")
    public  CreateMemberResponse saveMemverV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.name);
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemverV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemverRequest request) {

        memberService.update(id, request.name);
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result memberV2() {
        List<Member> members = memberService.findMembers();
        List<memberDto> collect = members.stream()
                .map(m -> new memberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(),collect);

    }
    @Data
    private class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long memberId) {
            this.id = memberId;
        }
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemverRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class memberDto {
        private String name;
    }
}
