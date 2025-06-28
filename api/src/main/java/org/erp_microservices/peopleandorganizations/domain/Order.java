package org.erp_microservices.peopleandorganizations.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"order\"")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    private UUID id;

    @Column(name = "order_identifier")
    private String orderIdentifier;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "order_type_id")
    private UUID orderTypeId;

    @Column(name = "placed_by_party_role_id")
    private UUID placedByPartyRoleId;

    @Column(name = "placed_using_contact_mechanism_id")
    private UUID placedUsingContactMechanismId;

    @Column(name = "taken_via_contact_mechanism_id")
    private UUID takenViaContactMechanismId;

    @Column(name = "taken_by_party_role_id")
    private UUID takenByPartyRoleId;

    @Column(name = "billing_location_contact_mechanism_id")
    private UUID billingLocationContactMechanismId;

    @Column(name = "with_requested_bill_to_party_role_id")
    private UUID withRequestedBillToPartyRoleId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "affectingOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderAdjustment> adjustments = new ArrayList<>();

    @OneToMany(mappedBy = "statusForOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderStatus> statusHistory = new ArrayList<>();

    public Order(UUID id, String orderIdentifier, LocalDate orderDate, UUID orderTypeId) {
        if (orderTypeId == null) {
            throw new IllegalArgumentException("Order type is required");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date is required");
        }

        this.id = id;
        this.orderIdentifier = orderIdentifier;
        this.orderDate = orderDate;
        this.orderTypeId = orderTypeId;
        this.entryDate = LocalDate.now();

        // Initialize with CREATED status
        addStatus(null); // Will need proper status type ID in full implementation
    }

    public OrderItem addItem(Long sequenceId, Long quantity, BigDecimal unitPrice, UUID productId) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price must be non-negative");
        }

        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setSequenceId(sequenceId);
        item.setQuantity(quantity);
        item.setUnitPrice(unitPrice);
        item.setProductId(productId);
        item.setOrder(this);
        item.setOrderTypeId(this.orderTypeId);

        this.items.add(item);
        return item;
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = items.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Apply adjustments
        for (OrderAdjustment adjustment : adjustments) {
            if (adjustment.getAmount() != null) {
                total = total.add(adjustment.getAmount());
            } else if (adjustment.getPercentage() != null) {
                BigDecimal adjustmentAmount = total.multiply(adjustment.getPercentage())
                    .divide(BigDecimal.valueOf(100));
                total = total.add(adjustmentAmount);
            }
        }

        return total;
    }

    public OrderAdjustment addAdjustment(UUID adjustmentTypeId, BigDecimal amount, BigDecimal percentage) {
        OrderAdjustment adjustment = new OrderAdjustment();
        adjustment.setId(UUID.randomUUID());
        adjustment.setOrderAdjustmentTypeId(adjustmentTypeId);
        adjustment.setAmount(amount);
        adjustment.setPercentage(percentage);
        adjustment.setAffectingOrder(this);

        this.adjustments.add(adjustment);
        return adjustment;
    }

    public OrderStatus getCurrentStatus() {
        return statusHistory.isEmpty() ? null : statusHistory.get(statusHistory.size() - 1);
    }

    public void updateStatus(UUID statusTypeId) {
        addStatus(statusTypeId);
    }

    private void addStatus(UUID statusTypeId) {
        OrderStatus status = new OrderStatus();
        status.setId(UUID.randomUUID());
        status.setOrderStatusTypeId(statusTypeId);
        status.setStatusChanged(LocalDateTime.now());
        status.setStatusForOrder(this);

        this.statusHistory.add(status);
    }
}