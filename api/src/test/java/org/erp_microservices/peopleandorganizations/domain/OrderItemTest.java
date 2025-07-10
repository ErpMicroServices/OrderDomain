package org.erp_microservices.peopleandorganizations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Item Domain Entity Tests")
class OrderItemTest {

    private OrderItem orderItem;
    private UUID orderId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        orderItem = new OrderItem();
        orderId = UUID.randomUUID();
        productId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        // Given
        UUID itemId = UUID.randomUUID();
        Long sequenceId = 1L;
        Long quantity = 10L;
        BigDecimal unitPrice = new BigDecimal("25.50");
        LocalDate deliveryDate = LocalDate.now().plusDays(7);
        String shippingInstructions = "Handle with care";
        String itemDescription = "Test product";
        String comment = "Rush order";
        UUID contactMechanismId = UUID.randomUUID();
        UUID orderTypeId = UUID.randomUUID();

        // When
        orderItem.setId(itemId);
        orderItem.setSequenceId(sequenceId);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(unitPrice);
        orderItem.setEstimatedDeliveryDate(deliveryDate);
        orderItem.setShippingInstructions(shippingInstructions);
        orderItem.setItemDescription(itemDescription);
        orderItem.setComment(comment);
        orderItem.setContactMechanismId(contactMechanismId);
        orderItem.setProductId(productId);
        orderItem.setOrderTypeId(orderTypeId);

        // Then
        assertThat(orderItem.getId()).isEqualTo(itemId);
        assertThat(orderItem.getSequenceId()).isEqualTo(sequenceId);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getUnitPrice()).isEqualByComparingTo(unitPrice);
        assertThat(orderItem.getEstimatedDeliveryDate()).isEqualTo(deliveryDate);
        assertThat(orderItem.getShippingInstructions()).isEqualTo(shippingInstructions);
        assertThat(orderItem.getItemDescription()).isEqualTo(itemDescription);
        assertThat(orderItem.getComment()).isEqualTo(comment);
        assertThat(orderItem.getContactMechanismId()).isEqualTo(contactMechanismId);
        assertThat(orderItem.getProductId()).isEqualTo(productId);
        assertThat(orderItem.getOrderTypeId()).isEqualTo(orderTypeId);
    }

    @Test
    @DisplayName("Should associate with order")
    void shouldAssociateWithOrder() {
        // Given
        Order order = new Order(orderId, "ORD-001", LocalDate.now(), UUID.randomUUID());

        // When
        orderItem.setOrder(order);

        // Then
        assertThat(orderItem.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should handle optional fields")
    void shouldHandleOptionalFields() {
        // Given
        orderItem.setId(UUID.randomUUID());
        orderItem.setSequenceId(1L);
        orderItem.setQuantity(5L);
        orderItem.setUnitPrice(new BigDecimal("10.00"));

        // Then
        assertThat(orderItem.getEstimatedDeliveryDate()).isNull();
        assertThat(orderItem.getShippingInstructions()).isNull();
        assertThat(orderItem.getItemDescription()).isNull();
        assertThat(orderItem.getComment()).isNull();
        assertThat(orderItem.getCorrespondingPoId()).isNull();
        assertThat(orderItem.getOrderedWithId()).isNull();
        assertThat(orderItem.getPartyRoleId()).isNull();
        assertThat(orderItem.getProductFeatureId()).isNull();
        assertThat(orderItem.getQuoteItemId()).isNull();
        assertThat(orderItem.getPlacingCustomerPartyRoleId()).isNull();
        assertThat(orderItem.getTakenByPartyRoleId()).isNull();
        assertThat(orderItem.getWithARequestedBillToPartyRoleId()).isNull();
    }

    @Test
    @DisplayName("Should set all party role fields")
    void shouldSetAllPartyRoleFields() {
        // Given
        UUID partyRoleId = UUID.randomUUID();
        UUID placingCustomerId = UUID.randomUUID();
        UUID takenById = UUID.randomUUID();
        UUID billToId = UUID.randomUUID();

        // When
        orderItem.setPartyRoleId(partyRoleId);
        orderItem.setPlacingCustomerPartyRoleId(placingCustomerId);
        orderItem.setTakenByPartyRoleId(takenById);
        orderItem.setWithARequestedBillToPartyRoleId(billToId);

        // Then
        assertThat(orderItem.getPartyRoleId()).isEqualTo(partyRoleId);
        assertThat(orderItem.getPlacingCustomerPartyRoleId()).isEqualTo(placingCustomerId);
        assertThat(orderItem.getTakenByPartyRoleId()).isEqualTo(takenById);
        assertThat(orderItem.getWithARequestedBillToPartyRoleId()).isEqualTo(billToId);
    }

    @Test
    @DisplayName("Should handle cross-references")
    void shouldHandleCrossReferences() {
        // Given
        UUID correspondingPoId = UUID.randomUUID();
        UUID orderedWithId = UUID.randomUUID();
        UUID quoteItemId = UUID.randomUUID();

        // When
        orderItem.setCorrespondingPoId(correspondingPoId);
        orderItem.setOrderedWithId(orderedWithId);
        orderItem.setQuoteItemId(quoteItemId);

        // Then
        assertThat(orderItem.getCorrespondingPoId()).isEqualTo(correspondingPoId);
        assertThat(orderItem.getOrderedWithId()).isEqualTo(orderedWithId);
        assertThat(orderItem.getQuoteItemId()).isEqualTo(quoteItemId);
    }
}