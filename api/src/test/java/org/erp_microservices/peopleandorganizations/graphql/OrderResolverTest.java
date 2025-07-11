package org.erp_microservices.peopleandorganizations.graphql;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderItem;
import org.erp_microservices.peopleandorganizations.graphql.OrderResolver.*;
import org.erp_microservices.peopleandorganizations.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order GraphQL Resolver Tests")
class OrderResolverTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderResolver orderResolver;

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
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @DisplayName("Should get order by ID")
        void shouldGetOrderById() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderService.findById(orderId)).thenReturn(Optional.of(order));

            // When
            Optional<Order> result = orderResolver.order(orderId);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(orderId);
            verify(orderService).findById(orderId);
        }

        @Test
        @DisplayName("Should get order by identifier")
        void shouldGetOrderByIdentifier() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderService.findByOrderIdentifier("ORD-001")).thenReturn(Optional.of(order));

            // When
            Optional<Order> result = orderResolver.orderByIdentifier("ORD-001");

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getOrderIdentifier()).isEqualTo("ORD-001");
            verify(orderService).findByOrderIdentifier("ORD-001");
        }

        @Test
        @DisplayName("Should get orders by date range")
        void shouldGetOrdersByDateRange() {
            // Given
            LocalDate startDate = LocalDate.of(2024, 1, 1);
            LocalDate endDate = LocalDate.of(2024, 1, 31);
            List<Order> orders = Arrays.asList(
                new Order(UUID.randomUUID(), "ORD-001", orderDate, orderTypeId),
                new Order(UUID.randomUUID(), "ORD-002", orderDate.plusDays(5), orderTypeId)
            );
            when(orderService.findByDateRange(startDate, endDate)).thenReturn(orders);

            // When
            List<Order> result = orderResolver.ordersByDateRange(startDate, endDate);

            // Then
            assertThat(result).hasSize(2);
            verify(orderService).findByDateRange(startDate, endDate);
        }

        @Test
        @DisplayName("Should get orders by customer")
        void shouldGetOrdersByCustomer() {
            // Given
            UUID customerId = UUID.randomUUID();
            List<Order> orders = Arrays.asList(new Order(orderId, "ORD-001", orderDate, orderTypeId));
            when(orderService.findByCustomer(customerId)).thenReturn(orders);

            // When
            List<Order> result = orderResolver.ordersByCustomer(customerId);

            // Then
            assertThat(result).hasSize(1);
            verify(orderService).findByCustomer(customerId);
        }

        @Test
        @DisplayName("Should get orders by status")
        void shouldGetOrdersByStatus() {
            // Given
            List<Order> orders = Arrays.asList(new Order(orderId, "ORD-001", orderDate, orderTypeId));
            when(orderService.findByStatus("CREATED")).thenReturn(orders);

            // When
            List<Order> result = orderResolver.ordersByStatus("CREATED");

            // Then
            assertThat(result).hasSize(1);
            verify(orderService).findByStatus("CREATED");
        }
    }

    @Nested
    @DisplayName("Mutation Operations")
    class MutationOperations {

        @Test
        @DisplayName("Should create sales order")
        void shouldCreateSalesOrder() {
            // Given
            CreateSalesOrderInput input = new CreateSalesOrderInput();
            input.setOrderIdentifier("SALES-001");
            input.setOrderDate(orderDate);
            input.setCustomerId(UUID.randomUUID());
            input.setBillingLocationId(UUID.randomUUID());

            Order order = new Order(orderId, "SALES-001", orderDate, orderTypeId);
            when(orderService.createSalesOrder(
                input.getOrderIdentifier(),
                input.getOrderDate(),
                input.getCustomerId(),
                input.getBillingLocationId()
            )).thenReturn(order);

            // When
            Order result = orderResolver.createSalesOrder(input);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOrderIdentifier()).isEqualTo("SALES-001");
            verify(orderService).createSalesOrder(
                input.getOrderIdentifier(),
                input.getOrderDate(),
                input.getCustomerId(),
                input.getBillingLocationId()
            );
        }

        @Test
        @DisplayName("Should create purchase order")
        void shouldCreatePurchaseOrder() {
            // Given
            CreatePurchaseOrderInput input = new CreatePurchaseOrderInput();
            input.setOrderIdentifier("PO-001");
            input.setOrderDate(orderDate);
            input.setSupplierId(UUID.randomUUID());
            input.setDeliveryLocationId(UUID.randomUUID());

            Order order = new Order(orderId, "PO-001", orderDate, orderTypeId);
            when(orderService.createPurchaseOrder(
                input.getOrderIdentifier(),
                input.getOrderDate(),
                input.getSupplierId(),
                input.getDeliveryLocationId()
            )).thenReturn(order);

            // When
            Order result = orderResolver.createPurchaseOrder(input);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getOrderIdentifier()).isEqualTo("PO-001");
            verify(orderService).createPurchaseOrder(
                input.getOrderIdentifier(),
                input.getOrderDate(),
                input.getSupplierId(),
                input.getDeliveryLocationId()
            );
        }

        @Test
        @DisplayName("Should add order item")
        void shouldAddOrderItem() {
            // Given
            AddOrderItemInput input = new AddOrderItemInput();
            input.setOrderId(orderId);
            input.setSequenceId(1);
            input.setQuantity(10);
            input.setUnitPrice(new BigDecimal("25.00"));
            input.setProductId(UUID.randomUUID());
            input.setDeliveryDate(LocalDate.now().plusDays(7));

            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID());
            orderItem.setSequenceId(1L);
            orderItem.setQuantity(10L);
            orderItem.setUnitPrice(new BigDecimal("25.00"));

            when(orderService.addOrderItem(
                input.getOrderId(),
                1L,
                10L,
                input.getUnitPrice(),
                input.getProductId(),
                input.getDeliveryDate()
            )).thenReturn(orderItem);

            // When
            OrderItem result = orderResolver.addOrderItem(input);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getSequenceId()).isEqualTo(1L);
            assertThat(result.getQuantity()).isEqualTo(10L);
            verify(orderService).addOrderItem(any(), eq(1L), eq(10L), any(), any(), any());
        }

        @Test
        @DisplayName("Should update order item quantity")
        void shouldUpdateOrderItemQuantity() {
            // Given
            UpdateOrderItemQuantityInput input = new UpdateOrderItemQuantityInput();
            input.setOrderId(orderId);
            input.setSequenceId(1);
            input.setNewQuantity(15);

            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderService.updateOrderItemQuantity(orderId, 1L, 15L)).thenReturn(order);

            // When
            Order result = orderResolver.updateOrderItemQuantity(input);

            // Then
            assertThat(result).isNotNull();
            verify(orderService).updateOrderItemQuantity(orderId, 1L, 15L);
        }

        @Test
        @DisplayName("Should remove order item")
        void shouldRemoveOrderItem() {
            // Given
            RemoveOrderItemInput input = new RemoveOrderItemInput();
            input.setOrderId(orderId);
            input.setSequenceId(1);

            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            when(orderService.removeOrderItem(orderId, 1L)).thenReturn(order);

            // When
            Order result = orderResolver.removeOrderItem(input);

            // Then
            assertThat(result).isNotNull();
            verify(orderService).removeOrderItem(orderId, 1L);
        }
    }

    @Nested
    @DisplayName("Schema Mapping Operations")
    class SchemaMappingOperations {

        @Test
        @DisplayName("Should calculate order total")
        void shouldCalculateOrderTotal() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);
            order.addItem(1L, 10L, new BigDecimal("25.00"), UUID.randomUUID());
            order.addItem(2L, 5L, new BigDecimal("50.00"), UUID.randomUUID());

            // When
            BigDecimal total = orderResolver.getOrderTotal(order);

            // Then
            assertThat(total).isEqualByComparingTo(new BigDecimal("500.00"));
        }

        @Test
        @DisplayName("Should get order type")
        void shouldGetOrderType() {
            // Given
            Order order = new Order(orderId, "ORD-001", orderDate, orderTypeId);

            // When
            String orderType = orderResolver.getOrderType(order);

            // Then
            assertThat(orderType).isEqualTo("SALES");
        }

        @Test
        @DisplayName("Should get item total")
        void shouldGetItemTotal() {
            // Given
            OrderItem item = new OrderItem();
            item.setQuantity(10L);
            item.setUnitPrice(new BigDecimal("25.00"));

            // When
            BigDecimal itemTotal = orderResolver.getItemTotal(item);

            // Then
            assertThat(itemTotal).isEqualByComparingTo(new BigDecimal("250.00"));
        }
    }
}