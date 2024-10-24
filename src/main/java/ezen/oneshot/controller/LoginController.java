package ezen.oneshot.controller;

import ezen.oneshot.domain.dao.Membership;
import ezen.oneshot.domain.dto.LoginForm;
import ezen.oneshot.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "user/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/loginForm";
        }
        Membership loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        // 글로벌 에러 : bindingResult.reject
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/loginForm";
        }
        log.info("loginMember: {}", loginMember);

        // request.getSession(); 로그인 성공시 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성, 디폴트가 true
        HttpSession session = request.getSession();
        log.info("session: {}", session);

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    // 로그아웃 기능
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();   // 세션 제거
        }
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다."); // 메시지 추가
        return "redirect:/";
    }


}
