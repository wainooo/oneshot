package ezen.oneshot.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sweetTaste;

    private int sourTaste;

    private int refreshing;

    private int bodyFeeling;

    private String predictedLiquor;

}
