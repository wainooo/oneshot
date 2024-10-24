package ezen.oneshot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ezen.oneshot.domain.dao.Preference;
import ezen.oneshot.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchDrinkService {

    private final PreferenceRepository preferenceRepository;

    // preference_id로 맛 가져오기
    public List<Preference> searchDrink(Long id) {
        List<Preference> result = new ArrayList<>();
        preferenceRepository.findById(id).ifPresent(result::add); // 값이 있으면 리스트에 추가
        return result; // 결과 반환
    }

    public List<String[]> readCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("static/csv/traditional_liquor.csv");
            try (InputStream inputStream = resource.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                 CSVReader csvReader = new CSVReader(inputStreamReader)) {

                return csvReader.readAll();

            } catch (CsvException e) {
                e.printStackTrace();
                throw new RuntimeException("CSV 파일을 읽는 중 문제가 발생했습니다.", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("CSV 파일을 찾을 수 없습니다.", e);
        }
    }

}
