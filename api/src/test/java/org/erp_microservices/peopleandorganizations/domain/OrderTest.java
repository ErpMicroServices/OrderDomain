package org.erp_microservices.peopleandorganizations.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Order Domain Entity Tests")
class OrderTest {

    private UUID orderId;
    private UUID orderTypeId;
    private LocalDate orderDate;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderTypeId = UUID.randomUUID();
        orderDate = LocalDate.of(2024, 1, 15);
    }

    @Nested
    @DisplayName("Order Creation")
    class OrderCreation {

        @Test
        @DisplayName("Should create order with valid data")
        void shouldCreateOrderWithValidData() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            
            assertThat(order.getId()).isEqualTo(orderId);
            assertThat(order.getOrderIdentifier()).isEqualTo("ORD-001");
            assertThat(order.getOrderDate()).isEqualTo(orderDate);
            assertThat(order.getOrderTypeId()).isEqualTo(orderTypeId);
            assertThat(order.getEntryDate()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("Should require order type")
        void shouldRequireOrderType() {
            assertThatThrownBy(() -> new Order(orderId, "ORD-001", orderDate, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order type is required");
        }

        @Test
        @DisplayName("Should require order date")
        void shouldRequireOrderDate() {
            assertThatThrownBy(() -> new Order(orderId, "ORD-001", null, orderTypeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Order date is required");
        }
    }

    @Nested
    @DisplayName("Order Items Management")
    class OrderItemsManagement {

        @Test
        @DisplayName("Should add order item with valid data")
        void shouldAddOrderItemWithValidData() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID productId = UUID.randomUUID();
            BigDecimal unitPrice = new BigDecimal("25.00");
            Long quantity = 10L;
            
            OrderItem item = order.addItem(1L, quantity, unitPrice, productId);
            
            assertThat(item.getSequenceId()).isEqualTo(1L);
            assertThat(item.getQuantity()).isEqualTo(quantity);
            assertThat(item.getUnitPrice()).isEqualTo(unitPrice);
            assertThat(item.getProductId()).isEqualTo(productId);
            assertThat(order.getItems()).hasSize(1);
        }

        @Test
        @DisplayName("Should calculate order total correctly")
        void shouldCalculateOrderTotalCorrectly() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID productId1 = UUID.randomUUID();
            UUID productId2 = UUID.randomUUID();
            
            order.addItem(1L, 10L, new BigDecimal("25.00"), productId1); // 250.00
            order.addItem(2L, 5L, new BigDecimal("50.00"), productId2);  // 250.00
            
            BigDecimal expectedTotal = new BigDecimal("500.00");
            assertThat(order.calculateTotal()).isEqualByComparingTo(expectedTotal);
        }

        @Test
        @DisplayName("Should reject negative quantities")
        void shouldRejectNegativeQuantities() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID productId = UUID.randomUUID();
            BigDecimal unitPrice = new BigDecimal("25.00");
            
            assertThatThrownBy(() -> order.addItem(1L, -5L, unitPrice, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be positive");
        }

        @Test
        @DisplayName("Should reject negative unit prices")
        void shouldRejectNegativeUnitPrices() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID productId = UUID.randomUUID();
            BigDecimal negativePrice = new BigDecimal("-10.00");
            
            assertThatThrownBy(() -> order.addItem(1L, 10L, negativePrice, productId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unit price must be non-negative");
        }
    }

    @Nested
    @DisplayName("Order Adjustments")
    class OrderAdjustments {

        @Test
        @DisplayName("Should add discount adjustment")
        void shouldAddDiscountAdjustment() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID adjustmentTypeId = UUID.randomUUID(); // Discount type
            BigDecimal discountPercentage = new BigDecimal("10.00");
            
            OrderAdjustment adjustment = order.addAdjustment(adjustmentTypeId, null, discountPercentage);
            
            assertThat(adjustment.getPercentage()).isEqualByComparingTo(discountPercentage);
            assertThat(adjustment.getOrderAdjustmentTypeId()).isEqualTo(adjustmentTypeId);
            assertThat(order.getAdjustments()).hasSize(1);
        }

        @Test
        @DisplayName("Should add fixed amount adjustment")
        void shouldAddFixedAmountAdjustment() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID adjustmentTypeId = UUID.randomUUID(); // Fee type
            BigDecimal fixedAmount = new BigDecimal("15.00");
            
            OrderAdjustment adjustment = order.addAdjustment(adjustmentTypeId, fixedAmount, null);
            
            assertThat(adjustment.getAmount()).isEqualByComparingTo(fixedAmount);
            assertThat(adjustment.getOrderAdjustmentTypeId()).isEqualTo(adjustmentTypeId);
        }
    }

    @Nested
    @DisplayName("Order Status Management")
    class OrderStatusManagement {

        @Test
        @DisplayName("Should start with CREATED status")
        void shouldStartWithCreatedStatus() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            
            assertThat(order.getCurrentStatus().getStatus()).isEqualTo("CREATED");
            assertThat(order.getCurrentStatus().getStatusChanged()).isNotNull();
        }

        @Test
        @DisplayName("Should update status with timestamp")
        void shouldUpdateStatusWithTimestamp() {
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID statusTypeId = UUID.randomUUID(); // PROCESSING status
            
            order.updateStatus(statusTypeId);
            
            assertThat(order.getCurrentStatus().getOrderStatusTypeId()).isEqualTo(statusTypeId);
            assertThat(order.getStatusHistory()).hasSize(2); // CREATED + PROCESSING
        }
    }
}