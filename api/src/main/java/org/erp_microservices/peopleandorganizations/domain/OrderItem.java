package org.erp_microservices.peopleandorganizations.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    private UUID id;

    @Column(name = "sequence_id")
    private Long sequenceId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "shipping_instructions")
    private String shippingInstructions;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "comment")
    private String comment;

    @Column(name = "contact_mechanism_id")
    private UUID contactMechanismId;

    @Column(name = "corresponding_po_id")
    private UUID correspondingPoId;

    @Column(name = "ordered_with_id")
    private UUID orderedWithId;

    @Column(name = "party_role_id")
    private UUID partyRoleId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_feature_id")
    private UUID productFeatureId;

    @Column(name = "order_type_id")
    private UUID orderTypeId;

    @Column(name = "quote_item_id")
    private UUID quoteItemId;

    @Column(name = "placing_customer_party_role_id")
    private UUID placingCustomerPartyRoleId;

    @Column(name = "taken_by_party_role_id")
    private UUID takenByPartyRoleId;

    @Column(name = "with_a_requested_bill_to_party_role_id")
    private UUID withARequestedBillToPartyRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}