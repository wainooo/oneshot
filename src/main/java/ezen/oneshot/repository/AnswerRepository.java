package ezen.oneshot.repository;

import ezen.oneshot.domain.dao.Answer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 회원 ID로 답변 목록 가져오기
    List<Answer> findByAuthorId(Long memberId, Sort sort);

    // 댓글 최신순으로 정렬
    List<Answer> findByQuestionId(long questionId, Sort sort);
}
