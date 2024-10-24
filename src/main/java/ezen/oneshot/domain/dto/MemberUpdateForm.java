package ezen.oneshot.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class MemberUpdateForm {

    private Long id;

    private String loginId;

    private String name;

    private LocalDate birthdate;

    private String email;

    @NotEmpty(message = "비밀번호 수정을 원한다면 입력은 필수입니다.")
    private String password;

    private boolean emailOptIn;
}
