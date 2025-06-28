package org.erp_microservices.peopleandorganizations;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Simplified test to verify basic Order functionality works
 * This bypasses the Spring Boot test configuration issues
 */
public class SimpleOrderTest {

    @Test
    void shouldCreateOrderWithValidData() {
        UUID orderId = UUID.randomUUID();
        UUID orderTypeId = UUID.randomUUID();
        LocalDate orderDate = LocalDate.of(2024, 1, 15);

        Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);

        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getOrderIdentifier()).isEqualTo("ORD-001");
        assertThat(order.getOrderDate()).isEqualTo(orderDate);
        assertThat(order.getOrderTypeId()).isEqualTo(orderTypeId);
        assertThat(order.getEntryDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldRequireOrderType() {
        UUID orderId = UUID.randomUUID();
        LocalDate orderDate = LocalDate.of(2024, 1, 15);

        assertThatThrownBy(() -> new Order(orderId, "ORD-001", orderDate, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Order type is required");
    }

    @Test
    void shouldAddItemsAndCalculateTotal() {
        UUID orderId = UUID.randomUUID();
        UUID orderTypeId = UUID.randomUUID();
        LocalDate orderDate = LocalDate.of(2024, 1, 15);

        Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
        
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        
        OrderItem item1 = order.addItem(1L, 10L, new BigDecimal("25.00"), productId1);
        OrderItem item2 = order.addItem(2L, 5L, new BigDecimal("50.00"), productId2);

        assertThat(order.getItems()).hasSize(2);
        assertThat(item1.getQuantity()).isEqualTo(10L);
        assertThat(item2.getQuantity()).isEqualTo(5L);
        
        BigDecimal total = order.calculateTotal();
        assertThat(total).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void shouldRejectNegativeQuantities() {
        UUID orderId = UUID.randomUUID();
        UUID orderTypeId = UUID.randomUUID();
        LocalDate orderDate = LocalDate.of(2024, 1, 15);

        Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
        UUID productId = UUID.randomUUID();

        assertThatThrownBy(() -> order.addItem(1L, -5L, new BigDecimal("25.00"), productId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Quantity must be positive");
    }

    @Test
    void shouldHaveInitialStatus() {
        UUID orderId = UUID.randomUUID();
        UUID orderTypeId = UUID.randomUUID();
        LocalDate orderDate = LocalDate.of(2024, 1, 15);

        Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);

        assertThat(order.getCurrentStatus()).isNotNull();
        assertThat(order.getCurrentStatus().getStatus()).isEqualTo("CREATED");
        assertThat(order.getStatusHistory()).hasSize(1);
    }
}