package org.erp_microservices.peopleandorganizations.repository;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
@DisplayName("Order Repository Integration Tests")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private UUID orderTypeId;
    private UUID customerId;
    private UUID supplierId;
    private LocalDate orderDate;

    @BeforeEach
    void setUp() {
        orderTypeId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        supplierId = UUID.randomUUID();
        orderDate = LocalDate.of(2024, 1, 15);
        
        // Clean up any existing data
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save and retrieve order")
    void shouldSaveAndRetrieveOrder() {
        // Given
        Order order = new Order(UUID.randomUUID(), "ORD-001", orderDate, orderTypeId);
        order.setPlacedByPartyRoleId(customerId);

        // When
        Order savedOrder = orderRepository.save(order);
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        // Then
        assertThat(retrievedOrder).isPresent();
        assertThat(retrievedOrder.get().getOrderIdentifier()).isEqualTo("ORD-001");
        assertThat(retrievedOrder.get().getOrderDate()).isEqualTo(orderDate);
    }

    @Test
    @DisplayName("Should find order by identifier")
    void shouldFindOrderByIdentifier() {
        // Given
        Order order = new Order(UUID.randomUUID(), "ORD-UNIQUE-001", orderDate, orderTypeId);
        orderRepository.save(order);

        // When
        Optional<Order> found = orderRepository.findByOrderIdentifier("ORD-UNIQUE-001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getOrderIdentifier()).isEqualTo("ORD-UNIQUE-001");
    }

    @Test
    @DisplayName("Should find orders by order type")
    void shouldFindOrdersByOrderType() {
        // Given
        UUID orderType1 = UUID.randomUUID();
        UUID orderType2 = UUID.randomUUID();
        
        Order order1 = new Order(UUID.randomUUID(), "ORD-001", orderDate, orderType1);
        Order order2 = new Order(UUID.randomUUID(), "ORD-002", orderDate, orderType1);
        Order order3 = new Order(UUID.randomUUID(), "ORD-003", orderDate, orderType2);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When
        List<Order> orders = orderRepository.findByOrderTypeId(orderType1);

        // Then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("ORD-001", "ORD-002");
    }

    @Test
    @DisplayName("Should find orders by date range")
    void shouldFindOrdersByDateRange() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);
        
        Order order1 = new Order(UUID.randomUUID(), "ORD-001", LocalDate.of(2024, 1, 15), orderTypeId);
        Order order2 = new Order(UUID.randomUUID(), "ORD-002", LocalDate.of(2024, 1, 20), orderTypeId);
        Order order3 = new Order(UUID.randomUUID(), "ORD-003", LocalDate.of(2024, 2, 1), orderTypeId);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When
        List<Order> orders = orderRepository.findByOrderDateBetween(startDate, endDate);

        // Then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("ORD-001", "ORD-002");
    }

    @Test
    @DisplayName("Should find orders by customer")
    void shouldFindOrdersByCustomer() {
        // Given
        UUID customer1 = UUID.randomUUID();
        UUID customer2 = UUID.randomUUID();
        
        Order order1 = new Order(UUID.randomUUID(), "ORD-001", orderDate, orderTypeId);
        order1.setPlacedByPartyRoleId(customer1);
        
        Order order2 = new Order(UUID.randomUUID(), "ORD-002", orderDate, orderTypeId);
        order2.setPlacedByPartyRoleId(customer1);
        
        Order order3 = new Order(UUID.randomUUID(), "ORD-003", orderDate, orderTypeId);
        order3.setPlacedByPartyRoleId(customer2);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When
        List<Order> orders = orderRepository.findByCustomer(customer1);

        // Then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("ORD-001", "ORD-002");
    }

    @Test
    @DisplayName("Should find orders by supplier")
    void shouldFindOrdersBySupplier() {
        // Given
        UUID supplier1 = UUID.randomUUID();
        UUID supplier2 = UUID.randomUUID();
        
        Order order1 = new Order(UUID.randomUUID(), "PO-001", orderDate, orderTypeId);
        order1.setTakenByPartyRoleId(supplier1);
        
        Order order2 = new Order(UUID.randomUUID(), "PO-002", orderDate, orderTypeId);
        order2.setTakenByPartyRoleId(supplier1);
        
        Order order3 = new Order(UUID.randomUUID(), "PO-003", orderDate, orderTypeId);
        order3.setTakenByPartyRoleId(supplier2);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When
        List<Order> orders = orderRepository.findBySupplier(supplier1);

        // Then
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("PO-001", "PO-002");
    }

    @Test
    @DisplayName("Should find orders by current status")
    void shouldFindOrdersByCurrentStatus() {
        // Given
        Order order1 = new Order(UUID.randomUUID(), "ORD-001", orderDate, orderTypeId);
        // Order1 starts with CREATED status by default
        
        Order order2 = new Order(UUID.randomUUID(), "ORD-002", orderDate, orderTypeId);
        // Order2 also starts with CREATED status
        
        Order order3 = new Order(UUID.randomUUID(), "ORD-003", orderDate, orderTypeId);
        order3.updateStatus(UUID.randomUUID()); // Update to different status
        OrderStatus processingStatus = order3.getCurrentStatus();
        processingStatus.setStatus("PROCESSING");
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // When
        List<Order> createdOrders = orderRepository.findByCurrentStatus("CREATED");

        // Then
        assertThat(createdOrders).hasSize(2);
        assertThat(createdOrders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("ORD-001", "ORD-002");
    }

    @Test
    @DisplayName("Should handle empty results gracefully")
    void shouldHandleEmptyResultsGracefully() {
        // When
        Optional<Order> notFound = orderRepository.findByOrderIdentifier("NON-EXISTENT");
        List<Order> noOrders = orderRepository.findByCustomer(UUID.randomUUID());
        List<Order> noOrdersInRange = orderRepository.findByOrderDateBetween(
            LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 12, 31)
        );

        // Then
        assertThat(notFound).isEmpty();
        assertThat(noOrders).isEmpty();
        assertThat(noOrdersInRange).isEmpty();
    }
}