package org.erp_microservices.peopleandorganizations.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_status")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatus {

    @Id
    private UUID id;

    @Column(name = "status_changed")
    private LocalDateTime statusChanged;

    @Column(name = "order_status_type_id")
    private UUID orderStatusTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_for_order_item_id")
    private OrderItem statusForOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_for_order_id")
    private Order statusForOrder;

    public String getStatus() {
        // This is a simplified implementation
        // In a full implementation, this would join with order_status_type table
        return "CREATED";
    }
}