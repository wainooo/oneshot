package ezen.oneshot.domain.dao;

import ezen.oneshot.repository.MemberRepository;
import ezen.oneshot.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class QuestionTest {

//    @Autowired
//    private QuestionRepository questionRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    // 대량 데이터 삽입하기
//    @Test
//    public void testJpa() {
//
//        Optional<Membership> member = memberRepository.findById(1l);
//
//        for (int i=1; i<=50; i++) {
//            Question question = new Question();
//            question.setSubject("Subject" + i);
//            question.setContent("Content" + i);
//            question.setAuthor(member.get());
//            question.setCreateDate(LocalDateTime.now());
//            questionRepository.save(question);
//        }
//    }

}