package com.gabezy.foodnow.domain.entity;

import com.gabezy.foodnow.domain.validated.CityValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class State {

    @NotNull(groups = CityValidation.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

}
