package ezen.oneshot.repository;

import ezen.oneshot.domain.dao.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    // preference_id로 맛 가져오기
    Optional<Preference> findById(Long id);
}
