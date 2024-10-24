package ezen.oneshot.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class YoutubeController {

    @GetMapping("/youtube")
    public String youtubeForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginMember = (session != null) ? session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        if (loginMember != null) {
            model.addAttribute("loginMember", loginMember);
        }
        return "youtubeBoard";
    }

    @GetMapping("/youtube/detail")
    public String youtubeDetail(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Object loginMember = (session != null) ? session.getAttribute(SessionConst.LOGIN_MEMBER) : null;

        if (loginMember != null) {
            model.addAttribute("loginMember", loginMember);
        }
        return "youtubeDetail";
    }

}
