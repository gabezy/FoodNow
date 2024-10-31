package com.gabezy.foodnow.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabezy.foodnow.core.validation.DeliveryFee;
import com.gabezy.foodnow.core.validation.FreeDeliveryIncludeDescription;
import com.gabezy.foodnow.domain.embedded.Address;
import com.gabezy.foodnow.domain.validated.RegisterRestaurant;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FreeDeliveryIncludeDescription( //validando o objeto
        valueField = "deliveryFee", checkedField= "name", requiredValue = "Free Delivery"
)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Restaurant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String name;

    @NotNull
//    @PositiveOrZero
    @DeliveryFee(message = "teste")
//    @Multiple(by = 5)
    @Column(name = "delivery_fee", nullable = false)
    private BigDecimal deliveryFee;

//    @JsonIgnore
//    @JsonIgnoreProperties("hibernateLazyInitializer") // ignore a serialização do uma propriedade específica
    // hibernateLazyInitializer -> é uma propriedade do classe de Proxy que o Hibernate cria quando utiliza o Lazy loading
    @Valid
    @ConvertGroup(from = Default.class, to = RegisterRestaurant.class)
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuisine_id", nullable = false)
    private Cuisine cuisine;

    @JsonIgnore
    @Embedded
    private Address address;

//    @JsonIgnore
    @CreationTimestamp // Coloca a timestamp quando criada (faz parte do Hibernate)
    @Column(nullable = false)
    private OffsetDateTime createdAt;

//    @JsonIgnore
    @UpdateTimestamp // Coloca a timestamp quando atualizada (faz parte do Hibernate)
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurant_payment_method", // Nomne da tabela intermediária
        joinColumns = @JoinColumn(name = "restaurant_id"), //  define o nome da coluna que faz referência a FK da entidade (Restaurant)
        inverseJoinColumns = @JoinColumn(name = "payment_method_id")) // define o nome da coluna que faz referência a FK da entidade inversa (PaymentMethod)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private Set<Product> products = new HashSet<>();

}
