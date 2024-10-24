package ezen.oneshot.repository;

import ezen.oneshot.domain.dao.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Membership, Long> {

    // 사용자 아이디로 조회하는 기능 추가
    Optional<Membership> findByLoginId(String loginId);
}
