package com.gabezy.foodnow.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabezy.foodnow.domain.validated.RegisterRestaurant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
public class Cuisine {

    @NotNull(groups = RegisterRestaurant.class)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String name;

    @JsonIgnore // Jackson ignora a serialização dessa propriedade
    @OneToMany(mappedBy = "cuisine") // mappedBy -> diz que a associação foi mapeada por pelo Restaurant (cuisine)
    private List<Restaurant> restaurants = new ArrayList<>();
}
