package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dto.PreferenceForm;
import ezen.oneshot.service.SearchDrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SearchDrinkController {

    private final SearchDrinkService searchDrinkService;


    @GetMapping("/searchdrink")
    public String showCsvPage(@RequestParam(value = "filter", required = false) String filter,
                              @RequestParam(value = "currentFilter", required = false) String currentFilter,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "sweetTaste", required = false) Integer sweetTaste,
                              @RequestParam(value = "sourTaste", required = false) Integer sourTaste,
                              @RequestParam(value = "refreshing", required = false) Integer refreshing,
                              @RequestParam(value = "bodyFeeling", required = false) Integer bodyFeeling,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember,
                              Model model) {
        List<String[]> csvData = searchDrinkService.readCsv();  // 예외는 서비스에서 처리됨

        if (csvData != null) {
            // 1번째 행부터 끝까지 데이터를 슬라이싱 (첫 번째 행은 헤더일 가능성이 높음)
            List<String[]> slicedData = csvData.subList(1, csvData.size());

            // 전체 데이터 개수
            int totalDataCount = csvData.size() - 1;

            // 필터 처리 로직: 필터 값이 있으면 필터링된 데이터를 표시
            if (filter != null && !filter.isEmpty()) {

                // 현재 필터와 새로운 필터가 동일하면 필터를 초기화 (전체 리스트를 보여줌)
                if (filter.equals(currentFilter)) {
                    currentFilter = null;  // 필터 초기화 (전체 리스트 표시)
                } else {
                    currentFilter = filter;  // 필터 값이 다르면 새 필터 적용
                }
            }

            // effectively final로 처리하기 위해 새로운 변수 선언
            final String appliedFilter = currentFilter;

            // 필터 값이 있는 경우 필터링된 데이터를 사용
            if (appliedFilter != null && !appliedFilter.isEmpty()) {
                slicedData = slicedData.stream()
                        .filter(row -> appliedFilter.equals(row[8]))  // 람다식에서 appliedFilter 사용, 주종비교하여 선택한 주종만 필터링
                        .collect(Collectors.toList());
            }

//                // 필터링된 데이터 개수
//                int filteredDataCount = slicedData.size();

//                // 페이지네이션 처리
//                int totalDataSize = slicedData.size();
//                int totalPages = (int) Math.ceil((double) totalDataSize / size);
//                int fromIndex = Math.min((page - 1) * size, totalDataSize);
//                int toIndex = Math.min(fromIndex + size, totalDataSize);

            int filteredDataCount = slicedData.size();
            int totalPages = (int) Math.ceil((double) filteredDataCount / size);

            // 페이지 번호 계산
            int fromIndex = Math.min(page * size, filteredDataCount);
            int toIndex = Math.min(fromIndex + size, filteredDataCount);

            List<String[]> paginatedData = slicedData.subList(fromIndex, toIndex);

            // 페이지네이션 범위 설정
            int pageDisplayRange = 5;  // 페이지 번호 표시 범위
            int startPage = Math.max(0, page - (pageDisplayRange / 2));
            int endPage = Math.min(totalPages - 1, startPage + pageDisplayRange - 1);

            // startPage와 endPage가 1 페이지 범위를 초과하지 않도록 조정
            if (endPage - startPage + 1 < pageDisplayRange) {
                startPage = Math.max(0, endPage - (pageDisplayRange - 1)); // startPage를 조정
            }

            model.addAttribute("csvData", paginatedData);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            model.addAttribute("currentFilter", currentFilter);
            model.addAttribute("startPage", startPage);  // 시작 페이지 번호
            model.addAttribute("endPage", endPage);      // 끝 페이지 번호
            model.addAttribute("totalDataCount", totalDataCount);  // 전체 데이터 개수
            model.addAttribute("filteredDataCount", filteredDataCount);  // 필터링된 데이터 개수
            // 모델에 사용자 입력 값 추가
            model.addAttribute("sweetTaste", sweetTaste);
            model.addAttribute("sourTaste", sourTaste);
            model.addAttribute("refreshing", refreshing);
            model.addAttribute("bodyFeeling", bodyFeeling);
            model.addAttribute("loginMember", loginMember);
        } else {
            model.addAttribute("errorMessage", "CSV 파일을 읽어오는 중 문제가 발생했습니다.");
        }

        model.addAttribute("pageTitle", "CSV 데이터");
        return "user/searchDrink";  // csvPage.html로 데이터 전달
    }
}
