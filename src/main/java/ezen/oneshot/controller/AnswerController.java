package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Question;
import ezen.oneshot.domain.dto.AnswerForm;
import ezen.oneshot.service.AnswerService;
import ezen.oneshot.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    // 게시글에 대한 댓글 쓰기
    @PostMapping("/answer/create/{questionid}")
    public String createAnswer(@PathVariable("questionid") Long questionid, @Valid AnswerForm answerForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {

        Optional<Question> question = questionService.getQuestion(questionid);

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question.get());
            model.addAttribute("loginMember", loginMember);
            return "user/talkBoardDetail";
        }
        if (question.isPresent()) {
            Answer answer = new Answer();
            answer.setQuestion(question.get());
            answer.setContent(answerForm.getContent());
            answer.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            answer.setAuthor(loginMember);
            answerService.create(answer);
        }
        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%s#comments", questionid);
    }

    // 댓글 수정 페이지 열기
    @GetMapping("/answer/modify/{answerId}")
    public String answerModify(@PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, AnswerForm answerForm, Model model) {
        Optional<Answer> answer = answerService.getAnswer(answerId);
        answerForm.setContent(answer.get().getContent());
        model.addAttribute("answerForm", answerForm);
        model.addAttribute("loginMember", loginMember);
        return "user/talkAnswerForm";
    }

    // 댓글 수정하기
    @PostMapping("/answer/modify/{answerId}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/talkAnswerForm";
        }

        Optional<Answer> findAnswer = answerService.getAnswer(answerId);
        Optional<Question> findQuestion = questionService.getQuestion(findAnswer.get().getQuestion().getId());

        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setContent(answerForm.getContent());
        answer.setCreateDate(findAnswer.get().getCreateDate());
        answer.setModifyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        answer.setAuthor(loginMember);
        answer.setQuestion(findQuestion.get());
        answerService.create(answer);

        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%s#comments", findQuestion.get().getId());
    }

    // 댓글 삭제하기
    @PostMapping("/answer/delete/{answerId}")
    public String answerDelete(@PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        Optional<Answer> deleteAnswer = answerService.getAnswer(answerId);
        if (deleteAnswer.isPresent()) {
            answerService.delete(deleteAnswer.get());
        }
        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%s", deleteAnswer.get().getQuestion().getId());
    }


}
