package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Question;
import ezen.oneshot.domain.dto.AnswerForm;
import ezen.oneshot.domain.dto.QuestionForm;
import ezen.oneshot.service.AnswerService;
import ezen.oneshot.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    // 질문등록폼 열기
    @GetMapping("/question/create")
    public String questionCreateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, QuestionForm questionForm, Model model) {
        if (loginMember != null) {
            model.addAttribute("questionForm", questionForm);
            model.addAttribute("loginMember", loginMember);
            return "user/talkQuestionForm";
        }
        return "redirect:/login";
    }

    // 질문 등록하기
    @PostMapping("/question/create")
    public String questionCreate(@Valid @ModelAttribute("questionForm") QuestionForm questionForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/talkQuestionForm";
        }

        if (loginMember != null) {
            Question question = new Question();
            question.setSubject(questionForm.getSubject());
            question.setContent(questionForm.getContent());
            question.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            question.setAuthor(loginMember);
            questionService.create(question);
            model.addAttribute("loginMember", loginMember);
            return "redirect:/question/list";
        }
        return "redirect:/login";
    }

    // 게시글 목록 보기
    @GetMapping("/question/list")
    public String list(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(value = "searchType", defaultValue = "title") String searchType, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        Page<Question> paging = questionService.getList(page, kw, searchType);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("searchType", searchType);
        return "talkBoardList";
    }

    // 게시글 상세 보기
    @GetMapping("/question/detail/{id}")
    public String detail(@PathVariable("id") Long id, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, AnswerForm answerForm, Model model) {
        if (loginMember != null) {
            Optional<Question> question = questionService.getQuestion(id);
            if (question.isPresent()) {
                model.addAttribute("question", question.get());
                model.addAttribute("loginMember", loginMember);
                model.addAttribute("answerForm", answerForm);

                // 댓글 목록을 최신순으로 가져오기
                List<Answer> answers = answerService.getAnswersSort(id);
                model.addAttribute("answers", answers); // 모델에 댓글 추가

                return "user/talkBoardDetail";
            }
            model.addAttribute("loginMember", loginMember);
            return "redirect:/question/list";
        }
        return "redirect:/login";
    }

    // 게시글 수정 페이지 열기
    @GetMapping("/question/modify/{questionId}")
    public String questionModify(@PathVariable("questionId") Long questionId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, QuestionForm questionForm, Model model) {
        Optional<Question> question = questionService.getQuestion(questionId);
        questionForm.setSubject(question.get().getSubject());
        questionForm.setContent(question.get().getContent());

        model.addAttribute("question", questionForm);
        model.addAttribute("loginMember", loginMember);
        return "user/talkQuestionForm";
    }

    // 게시글 질문 수정하기
    @PostMapping("/question/modify/{questionId}")
    public String questionModify(@Valid @ModelAttribute("questionForm") QuestionForm questionForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, @PathVariable("questionId") Long questionId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/talkQuestionForm";
        }
        Optional<Question> findQuestion = questionService.getQuestion(questionId);

        Question question = new Question();
        question.setId(questionId);
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setCreateDate(findQuestion.get().getCreateDate());
        question.setModifyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        question.setAuthor(loginMember);
        questionService.create(question);
        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%d", questionId);
    }

    // 게시글 질문 삭제하기
    @PostMapping("/question/delete/{questionId}")
    public String questionDelete(@PathVariable("questionId") Long questionId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember) {
        Optional<Question> deleteQuestion = questionService.getQuestion(questionId);
        if (deleteQuestion.isPresent()) {
            questionService.delete(deleteQuestion.get());
        }
        return "redirect:/question/list"; // 삭제 후 질문 목록 페이지로 리다이렉션
    }

}
