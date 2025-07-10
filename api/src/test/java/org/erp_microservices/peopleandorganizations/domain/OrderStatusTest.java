package org.erp_microservices.peopleandorganizations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Status Domain Entity Tests")
class OrderStatusTest {

    private OrderStatus orderStatus;

    @BeforeEach
    void setUp() {
        orderStatus = new OrderStatus();
    }

    @Test
    @DisplayName("Should set and get all properties")
    void shouldSetAndGetAllProperties() {
        // Given
        UUID statusId = UUID.randomUUID();
        UUID statusTypeId = UUID.randomUUID();
        LocalDateTime statusChanged = LocalDateTime.now();
        String status = "PROCESSING";

        // When
        orderStatus.setId(statusId);
        orderStatus.setOrderStatusTypeId(statusTypeId);
        orderStatus.setStatusChanged(statusChanged);
        orderStatus.setStatus(status);

        // Then
        assertThat(orderStatus.getId()).isEqualTo(statusId);
        assertThat(orderStatus.getOrderStatusTypeId()).isEqualTo(statusTypeId);
        assertThat(orderStatus.getStatusChanged()).isEqualTo(statusChanged);
        assertThat(orderStatus.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should associate with order")
    void shouldAssociateWithOrder() {
        // Given
        Order order = new Order(UUID.randomUUID(), "ORD-001", java.time.LocalDate.now(), UUID.randomUUID());

        // When
        orderStatus.setStatusForOrder(order);

        // Then
        assertThat(orderStatus.getStatusForOrder()).isEqualTo(order);
        assertThat(orderStatus.getStatusForOrderItem()).isNull();
    }

    @Test
    @DisplayName("Should associate with order item")
    void shouldAssociateWithOrderItem() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());

        // When
        orderStatus.setStatusForOrderItem(orderItem);

        // Then
        assertThat(orderStatus.getStatusForOrderItem()).isEqualTo(orderItem);
        assertThat(orderStatus.getStatusForOrder()).isNull();
    }

    @Test
    @DisplayName("Should return CREATED as default status when status is null")
    void shouldReturnCreatedAsDefaultStatusWhenNull() {
        // Given
        orderStatus.setStatus(null);

        // When
        String status = orderStatus.getStatus();

        // Then
        assertThat(status).isEqualTo("CREATED");
    }

    @Test
    @DisplayName("Should return actual status when set")
    void shouldReturnActualStatusWhenSet() {
        // Given
        orderStatus.setStatus("SHIPPED");

        // When
        String status = orderStatus.getStatus();

        // Then
        assertThat(status).isEqualTo("SHIPPED");
    }

    @Test
    @DisplayName("Should handle status transitions")
    void shouldHandleStatusTransitions() {
        // Given
        LocalDateTime firstChange = LocalDateTime.now().minusHours(2);
        LocalDateTime secondChange = LocalDateTime.now();

        // When
        orderStatus.setStatus("CREATED");
        orderStatus.setStatusChanged(firstChange);
        
        // Simulate status change
        orderStatus.setStatus("PROCESSING");
        orderStatus.setStatusChanged(secondChange);

        // Then
        assertThat(orderStatus.getStatus()).isEqualTo("PROCESSING");
        assertThat(orderStatus.getStatusChanged()).isEqualTo(secondChange);
        assertThat(orderStatus.getStatusChanged()).isAfter(firstChange);
    }
}