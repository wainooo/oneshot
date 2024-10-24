package ezen.oneshot.service;

import ezen.oneshot.domain.dao.Preference;
import ezen.oneshot.domain.dto.PreferenceForm;
import ezen.oneshot.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    // Preference 맛 저장하기
    public Preference savePreference(Preference preference) {
        return preferenceRepository.save(preference);
    }

    // ID로 Preference 맛 조회하기
    public List<Preference> getPreferenceById(Long id) {
        List<Preference> result = new ArrayList<>(); // 빈 리스트 생성
        preferenceRepository.findById(id).ifPresent(result::add); // 값이 있으면 리스트에 추가
        return result; // 결과 반환
    }

}
