package org.erp_microservices.peopleandorganizations.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_adjustment")
@Getter
@Setter
@NoArgsConstructor
public class OrderAdjustment {

    @Id
    private UUID id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "percentage")
    private BigDecimal percentage;

    @Column(name = "order_adjustment_type_id")
    private UUID orderAdjustmentTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affecting_order_item_id")
    private OrderItem affectingOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affecting_order_id")
    private Order affectingOrder;
}