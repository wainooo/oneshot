package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dao.Preference;
import ezen.oneshot.domain.dto.PreferenceForm;
import ezen.oneshot.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @GetMapping("/preference")
    public String preference(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        if (loginMember != null) {
            model.addAttribute("preferenceForm", new PreferenceForm());
            model.addAttribute("loginMember", loginMember);
            return "user/preferenceTest";
        }
        return "redirect:/login";
    }

    @PostMapping("/preference")
    public String postPreference(@ModelAttribute PreferenceForm form, RedirectAttributes redirectAttributes, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Membership loginMember, Model model) {
        Preference preference = new Preference();
        preference.setId(form.getId());
        preference.setSweetTaste(form.getSweetTaste());
        preference.setSourTaste(form.getSourTaste());
        preference.setRefreshing(form.getRefreshing());
        preference.setBodyFeeling(form.getBodyFeeling());
        preference.setPredictedLiquor(form.getPredictedLiquor());
        preferenceService.savePreference(preference);

        // redirectAttributes에 사용자가 선택한 값을 추가
        redirectAttributes.addAttribute("sweetTaste", form.getSweetTaste());
        redirectAttributes.addAttribute("sourTaste", form.getSourTaste());
        redirectAttributes.addAttribute("refreshing", form.getRefreshing());
        redirectAttributes.addAttribute("bodyFeeling", form.getBodyFeeling());
        redirectAttributes.addAttribute("currentFilter", form.getPredictedLiquor());
        redirectAttributes.addAttribute("fromPreferenceTest", true);
        model.addAttribute("loginMember", loginMember);

        return "redirect:/searchdrink";
    }
}
