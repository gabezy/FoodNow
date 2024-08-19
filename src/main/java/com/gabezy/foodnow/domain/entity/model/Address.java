package com.gabezy.foodnow.domain.entity.model;

import com.gabezy.foodnow.domain.entity.City;
import jakarta.persistence.*;
import lombok.Data;

// TODO mudar o package do classe? deveria está no entity?
@Data
@Embeddable // classe incorporável, que pode fazer parte de outra classe (Entidade).
            // Caso incorporada em outra entidada, na tabela da entidade terá os campos da classe (zipcode...)
public class Address {

    @Column(name = "address_zipcode")
    private String zipcode;

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_city_id")
    private City city;

}
