package ezen.oneshot.service;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Question;
import ezen.oneshot.repository.AnswerRepository;
import ezen.oneshot.repository.MemberRepository;
import ezen.oneshot.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    // 회원가입
    public Long join(Membership membership, BindingResult bindingResult) {
        // 중복회원 검증
        validateDuplicateMember(membership, bindingResult);

        // 나이 검증
        validateAge(membership, bindingResult);

        // 오류가 있는지 체크
        if (bindingResult.hasErrors()) {
            return null;
        }

        this.memberRepository.save(membership);
        return membership.getId();
    }

    // 나이 검증 메서드
    public void validateAge(Membership membership, BindingResult bindingResult) {
        LocalDate birthdate = membership.getBirthdate();

        if (birthdate != null) {
            int age = Period.between(birthdate, LocalDate.now()).getYears();
            if (age < 19) {
                bindingResult.rejectValue("birthdate", "error.age", "만 19세 이상만 가입할 수 있습니다.");
            }
        }
    }

    // 중복회원조회메서드
    public void validateDuplicateMember(Membership membership, BindingResult bindingResult) {
        Optional<Membership> findMember = memberRepository.findByLoginId(membership.getLoginId());
        if (findMember.isPresent()) {
            bindingResult.rejectValue("loginId", "error.loginId", "해당 ID는 이미 사용 중입니다. 다른 ID를 선택해 주세요.");
        }
    }

    // 전체 회원 조회
    public List<Membership> findAll() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회
    public  Optional<Membership> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 회원 삭제
    public void delete(Long memberId) {
        // 회원이 작성한 질문 삭제
        List<Question> questions = questionRepository.findByAuthorId(memberId, Sort.by(Sort.Direction.DESC, "createDate"));
        for (Question question : questions) {
            questionService.delete(question);
        }

        // 회원이 작성한 답변 삭제
        List<Answer> answers = answerRepository.findByAuthorId(memberId, Sort.by(Sort.Direction.DESC, "createDate"));
        for (Answer answer : answers) {
            answerService.delete(answer);
        }

        // 마지막으로 회원 삭제
        memberRepository.deleteById(memberId);
    }

    // 회원 수정
    public Long update(Membership membership) {
        // 회원 저장
        memberRepository.save(membership);
        return membership.getId();
    }

}
