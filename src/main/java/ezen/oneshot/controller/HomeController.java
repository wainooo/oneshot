package ezen.oneshot.controller;

import ezen.oneshot.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginMember = (session != null) ? session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        if (loginMember != null) {
            model.addAttribute("loginMember", loginMember);
        }

        return "home";
    }
}
