package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dto.MemberForm;
import ezen.oneshot.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public String addMemeber(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "user/addMemberForm";
    }

    @PostMapping("/add")
    public String create(@Validated @ModelAttribute("memberForm") MemberForm form, BindingResult bindingResult) {
        // 회원가입 로직
        Membership membership = new Membership();
        membership.setLoginId(form.getLoginId());
        membership.setPassword(form.getPassword());
        membership.setName(form.getName());
        membership.setBirthdate(form.getBirthdate());
        membership.setGender(form.getGender());
        membership.setEmail(form.getEmail());
        membership.setEmailOptIn(form.isEmailOptIn());

        // 나이 검증 로직 호출
        memberService.validateAge(membership, bindingResult);
        // 중복회원 검증 로직 호출
        memberService.validateDuplicateMember(membership, bindingResult);

        // 검증 후 오류가 있을 경우 다시 폼으로 돌아감
        if (bindingResult.hasErrors()) {
            return "user/addMemberForm"; // 오류가 있을 경우 폼으로 돌아갑니다.
        }

        // 회원가입 로직 호출
        memberService.join(membership, bindingResult);

        return "redirect:/";
    }

    // 회원 목록 보기 - 관리자 Page
    @GetMapping("/members")
    public String list(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        // 로그인한 사용자가 admin인지 확인
        if (loginMember == null || !loginMember.getLoginId().equals("admin")) {
            // admin이 아니면 홈 페이지로 리다이렉트
            return "redirect:/";
        }

        // admin일 경우 회원 목록을 가져와서 모델에 추가
        List<Membership> members = memberService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("loginMember", loginMember);
        return "admin/memberList";
    }

    // 회원 삭제 하기 - 관리자 Page
    @PostMapping("/members/{memberid}/delete")
    public String delete(@PathVariable(value = "memberid") Long memberid) {
        Optional<Membership> findMember = memberService.findOne(memberid);

        if (findMember.isPresent()) {
            memberService.delete(memberid);
        }
        return "redirect:/members";
    }

}
