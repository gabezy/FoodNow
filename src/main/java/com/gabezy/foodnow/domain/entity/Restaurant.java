package com.gabezy.foodnow.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gabezy.foodnow.domain.entity.model.Address;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "delivery_fee", nullable = false)
    private BigDecimal deliveryFee;

//    @JsonIgnore
//    @JsonIgnoreProperties("hibernateLazyInitializer") // ignore a serialização do uma propriedade específica
    // hibernateLazyInitializer -> é uma propriedade do classe de Proxy que o Hibernate cria quando utiliza o Lazy loading
    @ManyToOne
    @JoinColumn(name = "cuisine_id", nullable = false)
    private Cuisine cuisine;

    @JsonIgnore
    @Embedded
    private Address address;

    @JsonIgnore
    @CreationTimestamp // Coloca a timestamp quando criada (faz parte do Hibernate)
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp // Coloca a timestamp quando atualizada (faz parte do Hibernate)
    private LocalDateTime updatedAt;

//    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurant_payment_method", // Nomne da tabela intermediária
        joinColumns = @JoinColumn(name = "restaurant_id"), //  define o nome da coluna que faz referência a FK da entidade (Restaurant)
        inverseJoinColumns = @JoinColumn(name = "payment_method_id")) // define o nome da coluna que faz referência a FK da entidade inversa (PaymentMethod)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private Set<Product> products = new HashSet<>();

}
