package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Question;
import ezen.oneshot.domain.dto.MemberUpdateForm;
import ezen.oneshot.service.AnswerService;
import ezen.oneshot.service.MemberService;
import ezen.oneshot.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    // 마이페이지 회원정보 가져오기
    @GetMapping("/members/{memberid}/mypage")
    public String myPageGetForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, @PathVariable("memberid") Long memberid, Model model) {
        Optional<Membership> myMemberOne = memberService.findOne(memberid);

        if (myMemberOne.isPresent()) {
            Membership myMemberGet = myMemberOne.get();

            MemberUpdateForm memberUpdateForm = new MemberUpdateForm();
            memberUpdateForm.setLoginId(myMemberGet.getLoginId());
            memberUpdateForm.setName(myMemberGet.getName());
            memberUpdateForm.setBirthdate(myMemberGet.getBirthdate());
            memberUpdateForm.setEmail(myMemberGet.getEmail());
            memberUpdateForm.setEmailOptIn(myMemberGet.isEmailOptIn());

            model.addAttribute("loginMember", loginMember);
            model.addAttribute("memberUpdateForm", memberUpdateForm);

            // 질문 리스트와 답변 리스트 가져오기
            List<Question> questionList = questionService.getQuestions(memberid);
            List<Answer> answerList = answerService.getAnswers(memberid);

            // Set을 사용하여 질문 ID를 기준으로 중복 제거
            List<Answer> distinctAnswers = answerList.stream()
                    .filter(new HashSet<>()::add) // Set을 사용해 중복 제거
                    .collect(Collectors.toList());

            model.addAttribute("questionList", questionList);
            model.addAttribute("answerList", distinctAnswers);
            return "user/myPage";
        }
        model.addAttribute("loginMember", loginMember);
        return "redirect:/";
    }

    // 마이페이지 회원정보 가져오기
    @GetMapping("/members/{memberid}/mypage/update")
    public String myPageUpdateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, @PathVariable("memberid") Long memberid, Model model) {
        Optional<Membership> myMemberOne = memberService.findOne(memberid);

        if (myMemberOne.isPresent()) {
            Membership myMemberGet = myMemberOne.get();

            MemberUpdateForm memberUpdateForm = new MemberUpdateForm();
            memberUpdateForm.setLoginId(myMemberGet.getLoginId());
            memberUpdateForm.setName(myMemberGet.getName());
            memberUpdateForm.setBirthdate(myMemberGet.getBirthdate());
            memberUpdateForm.setEmail(myMemberGet.getEmail());
            memberUpdateForm.setEmailOptIn(myMemberGet.isEmailOptIn());

            model.addAttribute("loginMember", loginMember);
            model.addAttribute("memberUpdateForm", memberUpdateForm);

            return "user/myPageUpdate";
        }
        model.addAttribute("loginMember", loginMember);
        return "redirect:/";
    }

    // 회원 정보 수정하고 저장하기
    @PostMapping("/members/{memberid}/mypage/update")
    public String updateMember(@Validated @ModelAttribute("memberUpdateForm") MemberUpdateForm form, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {

        System.out.println("회원 정보 수정 요청 받음: " + form);

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            model.addAttribute("memberUpdateForm", form);
            model.addAttribute("loginMember", loginMember);
            return "user/myPageUpdate";
        }

        // 기존 회원 정보를 가져옵니다
        Optional<Membership> existingMemberOptional = memberService.findOne(loginMember.getId());
        if (existingMemberOptional.isPresent()) {
            Membership existingMember = existingMemberOptional.get();

            // 비밀번호가 입력된 경우에만 수정
            if (form.getPassword() != null && !form.getPassword().isEmpty()) {
                existingMember.setPassword(form.getPassword());
                System.out.println("비밀번호 업데이트됨.");
            }

            // 이메일 수정
            existingMember.setEmail(form.getEmail());
            System.out.println("이메일 업데이트됨: " + form.getEmail());

            // 이메일 수신 동의 업데이트
            existingMember.setEmailOptIn(form.isEmailOptIn());

            // 회원 정보를 업데이트합니다
            memberService.update(existingMember);
            System.out.println("회원 정보 업데이트 완료: " + existingMember);

            model.addAttribute("memberUpdateForm", form); // 현재 입력값 추가
            model.addAttribute("successMessage", "회원 정보가 성공적으로 업데이트되었습니다.");
        } else {
            System.out.println("회원 정보를 찾을 수 없습니다: " + form.getId());
            model.addAttribute("updateError", "회원 정보를 찾을 수 없습니다.");
        }

        return "redirect:/";
    }

}
