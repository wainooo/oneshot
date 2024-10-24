package ezen.oneshot.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "loginid")
    private String loginId;

    private String password;

    private String name;

    private LocalDate birthdate;

    private String gender;

    private String email;

    @Column(name = "emailoptin")
    private boolean emailOptIn;
}
