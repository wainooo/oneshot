package ezen.oneshot.service;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    // 댓글 생성
    public void create(Answer answer) {
        answerRepository.save(answer);
    }

    // 특정 댓글 조회
    public Optional<Answer> getAnswer(long id) {
        return answerRepository.findById(id);
    }

    // 회원 ID로 답변 리스트 가져오기
    public List<Answer> getAnswers(Long memberId) {
        return answerRepository.findByAuthorId(memberId, Sort.by(Sort.Direction.DESC, "createDate"));
    }

    // 특정 질문에 대한 댓글 조회 메소드 추가
    public List<Answer> getAnswersSort(Long questionId) {
        return answerRepository.findByQuestionId(questionId, Sort.by(Sort.Order.desc("createDate")));
    }

    // 댓글 삭제하기
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
