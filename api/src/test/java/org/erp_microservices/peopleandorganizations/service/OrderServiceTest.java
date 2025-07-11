package org.erp_microservices.peopleandorganizations.service;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderItem;
import org.erp_microservices.peopleandorganizations.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private UUID orderId;
    private UUID orderTypeId;
    private UUID customerId;
    private UUID supplierId;
    private UUID billingLocationId;
    private UUID deliveryLocationId;
    private LocalDate orderDate;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderTypeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        supplierId = UUID.randomUUID();
        billingLocationId = UUID.randomUUID();
        deliveryLocationId = UUID.randomUUID();
        orderDate = LocalDate.of(2024, 1, 15);
    }

    @Nested
    @DisplayName("Order Creation")
    class OrderCreation {

        @Test
        @DisplayName("Should create basic order")
        void shouldCreateBasicOrder() {
            // Given
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.createOrder("ORD-001", orderDate, orderTypeId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOrderIdentifier()).isEqualTo("ORD-001");
            assertThat(result.getOrderDate()).isEqualTo(orderDate);
            assertThat(result.getOrderTypeId()).isEqualTo(orderTypeId);
            verify(orderRepository).save(any(Order.class));
        }

        @Test
        @DisplayName("Should create sales order with customer details")
        void shouldCreateSalesOrder() {
            // Given
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.createSalesOrder("SALES-001", orderDate, customerId, billingLocationId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOrderIdentifier()).isEqualTo("SALES-001");
            assertThat(result.getPlacedByPartyRoleId()).isEqualTo(customerId);
            assertThat(result.getBillingLocationContactMechanismId()).isEqualTo(billingLocationId);
            
            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(orderCaptor.capture());
            assertThat(orderCaptor.getValue().getPlacedByPartyRoleId()).isEqualTo(customerId);
        }

        @Test
        @DisplayName("Should create purchase order with supplier details")
        void shouldCreatePurchaseOrder() {
            // Given
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.createPurchaseOrder("PO-001", orderDate, supplierId, deliveryLocationId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOrderIdentifier()).isEqualTo("PO-001");
            assertThat(result.getTakenByPartyRoleId()).isEqualTo(supplierId);
            assertThat(result.getTakenViaContactMechanismId()).isEqualTo(deliveryLocationId);
            verify(orderRepository).save(any(Order.class));
        }
    }

    @Nested
    @DisplayName("Order Retrieval")
    class OrderRetrieval {

        @Test
        @DisplayName("Should find order by ID")
        void shouldFindOrderById() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // When
            Optional<Order> result = orderService.findById(orderId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(orderId);
            verify(orderRepository).findById(orderId);
        }

        @Test
        @DisplayName("Should find order by identifier")
        void shouldFindOrderByIdentifier() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderRepository.findByOrderIdentifier("ORD-001")).thenReturn(Optional.of(order));

            // When
            Optional<Order> result = orderService.findByOrderIdentifier("ORD-001");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getOrderIdentifier()).isEqualTo("ORD-001");
            verify(orderRepository).findByOrderIdentifier("ORD-001");
        }

        @Test
        @DisplayName("Should find orders by date range")
        void shouldFindOrdersByDateRange() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            LocalDate endDate = LocalDate.of(2024, 1, 31);
            List<Order> orders = Arrays.asList(
                new Order(UUID.randomUUID(), "ORD-001", orderDate, orderTypeId),
                new Order(UUID.randomUUID(), "ORD-002", orderDate.plusDays(5), orderTypeId)
            );
            when(orderRepository.findByOrderDateBetween(startDate, endDate)).thenReturn(orders);

            // When
            List<Order> result = orderService.findByDateRange(startDate, endDate);

            // Then
            assertThat(result).hasSize(2);
            verify(orderRepository).findByOrderDateBetween(startDate, endDate);
        }

        @Test
        @DisplayName("Should find orders by customer")
        void shouldFindOrdersByCustomer() {
            // Given
            List<Order> orders = Arrays.asList(new Order(orderId, "ORD-001", orderDate, orderTypeId));
            when(orderRepository.findByCustomer(customerId)).thenReturn(orders);

            // When
            List<Order> result = orderService.findByCustomer(customerId);

            // Then
            assertThat(result).hasSize(1);
            verify(orderRepository).findByCustomer(customerId);
        }

        @Test
        @DisplayName("Should find orders by status")
        void shouldFindOrdersByStatus() {
            // Given
            List<Order> orders = Arrays.asList(new Order(orderId, "ORD-001", orderDate, orderTypeId));
            when(orderRepository.findByCurrentStatus("CREATED")).thenReturn(orders);

            // When
            List<Order> result = orderService.findByStatus("CREATED");

            // Then
            assertThat(result).hasSize(1);
            verify(orderRepository).findByCurrentStatus("CREATED");
        }
    }

    @Nested
    @DisplayName("Order Item Management")
    class OrderItemManagement {

        @Test
        @DisplayName("Should add order item")
        void shouldAddOrderItem() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID productId = UUID.randomUUID();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            OrderItem result = orderService.addOrderItem(
                orderId, 1L, 10L, new BigDecimal("25.00"), productId, LocalDate.now().plusDays(7)
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getSequenceId()).isEqualTo(1L);
            assertThat(result.getQuantity()).isEqualTo(10L);
            assertThat(result.getUnitPrice()).isEqualByComparingTo(new BigDecimal("25.00"));
            assertThat(result.getProductId()).isEqualTo(productId);
            verify(orderRepository).save(order);
        }

        @Test
        @DisplayName("Should throw exception when order not found for adding item")
        void shouldThrowExceptionWhenOrderNotFoundForAddingItem() {
            // Given
            UUID nonExistentOrderId = UUID.randomUUID();
            when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> orderService.addOrderItem(
                nonExistentOrderId, 1L, 10L, new BigDecimal("25.00"), UUID.randomUUID(), LocalDate.now()
            ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order not found");
        }

        @Test
        @DisplayName("Should update order item quantity")
        void shouldUpdateOrderItemQuantity() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            order.addItem(1L, 10L, new BigDecimal("25.00"), UUID.randomUUID());
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.updateOrderItemQuantity(orderId, 1L, 15L);

            // Then
            assertThat(result.getItems().get(0).getQuantity()).isEqualTo(15L);
            verify(orderRepository).save(order);
        }

        @Test
        @DisplayName("Should throw exception for invalid quantity update")
        void shouldThrowExceptionForInvalidQuantityUpdate() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            order.addItem(1L, 10L, new BigDecimal("25.00"), UUID.randomUUID());
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // When/Then
            assertThatThrownBy(() -> orderService.updateOrderItemQuantity(orderId, 1L, -5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quantity must be positive");
        }

        @Test
        @DisplayName("Should remove order item")
        void shouldRemoveOrderItem() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            order.addItem(1L, 10L, new BigDecimal("25.00"), UUID.randomUUID());
            order.addItem(2L, 5L, new BigDecimal("50.00"), UUID.randomUUID());
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.removeOrderItem(orderId, 1L);

            // Then
            assertThat(result.getItems()).hasSize(1);
            assertThat(result.getItems().get(0).getSequenceId()).isEqualTo(2L);
            verify(orderRepository).save(order);
        }
    }

    @Nested
    @DisplayName("Order Status Management")
    class OrderStatusManagement {

        @Test
        @DisplayName("Should update order status")
        void shouldUpdateOrderStatus() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            UUID newStatusTypeId = UUID.randomUUID();
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            Order result = orderService.updateOrderStatus(orderId, newStatusTypeId);

            // Then
            assertThat(result.getStatusHistory()).hasSize(2); // Initial + new status
            verify(orderRepository).save(order);
        }
    }

    @Nested
    @DisplayName("Order Calculations")
    class OrderCalculations {

        @Test
        @DisplayName("Should calculate order total")
        void shouldCalculateOrderTotal() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            order.addItem(1L, 10L, new BigDecimal("25.00"), UUID.randomUUID());
            order.addItem(2L, 5L, new BigDecimal("50.00"), UUID.randomUUID());
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

            // When
            BigDecimal total = orderService.calculateOrderTotal(orderId);

            // Then
            assertThat(total).isEqualByComparingTo(new BigDecimal("500.00"));
            verify(orderRepository).findById(orderId);
        }

        @Test
        @DisplayName("Should throw exception when order not found for total calculation")
        void shouldThrowExceptionWhenOrderNotFoundForTotalCalculation() {
            // Given
            UUID nonExistentOrderId = UUID.randomUUID();
            when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> orderService.calculateOrderTotal(nonExistentOrderId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Order not found");
        }
    }
}
