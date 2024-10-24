package ezen.oneshot.service;

import ezen.oneshot.domain.dao.Answer;
import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Question;
import ezen.oneshot.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 생성시 저장하기
    public void create(Question question) {
        questionRepository.save(question);
    }

    // 전체 게시글 조회
    public Page<Question> getList(int page, String kw, String searchType) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        PageRequest pageable = PageRequest.of(page, 15, Sort.by(sorts));

        // 제목으로 검색
        if ("title".equals(searchType)) {
            return questionRepository.findBySubjectContaining(kw, pageable);
        }
        // 내용으로 검색
        else if ("content".equals(searchType)) {
            return questionRepository.findByContentContaining(kw, pageable);
        }

        // 기본적으로 모든 질문을 조회
        return questionRepository.findAll(pageable);
    }

    // 게시글 조회
    public Optional<Question> getQuestion(long id) {
        return questionRepository.findById(id);
    }

    // 회원 ID로 게시글 리스트 가져오기
    public List<Question> getQuestions(Long memberId) {
        return questionRepository.findByAuthorId(memberId, Sort.by(Sort.Direction.DESC, "createDate"));
    }

    // 게시글 삭제 하기
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    // 검색 기능
    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            
            // 직렬화 과정에서의 일관성 보장
            private static final long serialVersionUID = 1L;
            
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 중복제거
                query.distinct(true);
                Join<Question, Membership> m1 = q.join("author", JoinType.LEFT);        // 질문 작성자 검색
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);     // 답변 내용 검색
                Join<Question, Membership> m2 = q.join("author", JoinType.LEFT);        // 답변 작성자 검색
                return criteriaBuilder.or(
                        criteriaBuilder.like(q.get("subject"), "%"+kw+"%"), // 제목
                        criteriaBuilder.like(q.get("content"), "%"+kw+"%"),           // 질문 내용
                        criteriaBuilder.like(m1.get("name"), "%"+kw+"%"),             // 질문 작성자
                        criteriaBuilder.like(a.get("content"), "%"+kw+"%"),           // 답변 내용
                        criteriaBuilder.like(m2.get("name"), "%"+kw+"%")              // 답변 작성자
                );
            }
        };
    }

}
