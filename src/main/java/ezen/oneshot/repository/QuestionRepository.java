package ezen.oneshot.repository;

import ezen.oneshot.domain.dao.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 제목으로 찾기
    Page<Question> findBySubjectContaining(String subject, Pageable pageable);

    // 내용으로 찾기
    Page<Question> findByContentContaining(String content, Pageable pageable);

    // 회원 ID로 질문 목록 가져오기
    List<Question> findByAuthorId(Long memberId, Sort sort);

    // findAll 메서드
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}
