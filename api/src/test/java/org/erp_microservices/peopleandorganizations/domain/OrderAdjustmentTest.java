package org.erp_microservices.peopleandorganizations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Adjustment Domain Entity Tests")
class OrderAdjustmentTest {

    private OrderAdjustment adjustment;

    @BeforeEach
    void setUp() {
        adjustment = new OrderAdjustment();
    }

    @Test
    @DisplayName("Should set and get fixed amount adjustment")
    void shouldSetAndGetFixedAmountAdjustment() {
        // Given
        UUID adjustmentId = UUID.randomUUID();
        UUID adjustmentTypeId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("15.00");

        // When
        adjustment.setId(adjustmentId);
        adjustment.setOrderAdjustmentTypeId(adjustmentTypeId);
        adjustment.setAmount(amount);

        // Then
        assertThat(adjustment.getId()).isEqualTo(adjustmentId);
        assertThat(adjustment.getOrderAdjustmentTypeId()).isEqualTo(adjustmentTypeId);
        assertThat(adjustment.getAmount()).isEqualByComparingTo(amount);
        assertThat(adjustment.getPercentage()).isNull();
    }

    @Test
    @DisplayName("Should set and get percentage adjustment")
    void shouldSetAndGetPercentageAdjustment() {
        // Given
        UUID adjustmentId = UUID.randomUUID();
        UUID adjustmentTypeId = UUID.randomUUID();
        BigDecimal percentage = new BigDecimal("10.00");

        // When
        adjustment.setId(adjustmentId);
        adjustment.setOrderAdjustmentTypeId(adjustmentTypeId);
        adjustment.setPercentage(percentage);

        // Then
        assertThat(adjustment.getId()).isEqualTo(adjustmentId);
        assertThat(adjustment.getOrderAdjustmentTypeId()).isEqualTo(adjustmentTypeId);
        assertThat(adjustment.getPercentage()).isEqualByComparingTo(percentage);
        assertThat(adjustment.getAmount()).isNull();
    }

    @Test
    @DisplayName("Should associate with order")
    void shouldAssociateWithOrder() {
        // Given
        Order order = new Order(UUID.randomUUID(), "ORD-001", java.time.LocalDate.now(), UUID.randomUUID());

        // When
        adjustment.setAffectingOrder(order);

        // Then
        assertThat(adjustment.getAffectingOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Should associate with order item")
    void shouldAssociateWithOrderItem() {
        // Given
        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());

        // When
        adjustment.setAffectingOrderItem(orderItem);

        // Then
        assertThat(adjustment.getAffectingOrderItem()).isEqualTo(orderItem);
    }

    @Test
    @DisplayName("Should handle both amount and percentage")
    void shouldHandleBothAmountAndPercentage() {
        // Given
        BigDecimal amount = new BigDecimal("20.00");
        BigDecimal percentage = new BigDecimal("5.00");

        // When
        adjustment.setAmount(amount);
        adjustment.setPercentage(percentage);

        // Then
        assertThat(adjustment.getAmount()).isEqualByComparingTo(amount);
        assertThat(adjustment.getPercentage()).isEqualByComparingTo(percentage);
    }

    @Test
    @DisplayName("Should handle negative adjustments")
    void shouldHandleNegativeAdjustments() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-10.00");
        BigDecimal negativePercentage = new BigDecimal("-5.00");

        // When
        adjustment.setAmount(negativeAmount);
        adjustment.setPercentage(negativePercentage);

        // Then
        assertThat(adjustment.getAmount()).isEqualByComparingTo(negativeAmount);
        assertThat(adjustment.getPercentage()).isEqualByComparingTo(negativePercentage);
    }
}