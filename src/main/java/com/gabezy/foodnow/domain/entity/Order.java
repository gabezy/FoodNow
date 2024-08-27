package com.gabezy.foodnow.domain.entity;

import com.gabezy.foodnow.domain.embedded.Address;
import com.gabezy.foodnow.domain.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "order_")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(name = "delivery_fee", nullable = false)
    private BigDecimal delivryFee;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "confirmed_at")
    private LocalDate confirmedAt;

    @Column(name = "canceled_at")
    private LocalDate canceledAt;

    @Column(name = "delivered_at")
    private LocalDate deliveredAt;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne // muitos pedidos para um usuário, mas não muitos usuários para um pedido
    @JoinColumn(name = "user_client_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order")
    private List<ItemOrder> itemOrders = new ArrayList<>();


}
