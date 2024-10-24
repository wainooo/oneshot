package ezen.oneshot.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PreferenceForm {

    private Long id;

    @NotNull
    private Integer sweetTaste;

    @NotNull
    private Integer sourTaste;

    @NotNull
    private Integer refreshing;

    @NotNull
    private Integer bodyFeeling;

    private String predictedLiquor;
}
