package org.erp_microservices.peopleandorganizations.integration;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("integration")
@Tag("integration")
@DisplayName("Order Repository Integration Test")
class OrderRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should save and retrieve order")
    void shouldSaveAndRetrieveOrder() {
        // Given
        UUID orderTypeId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), "ORD-001", LocalDate.now(), orderTypeId);

        // When
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        // Then
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderIdentifier()).isEqualTo("ORD-001");
        assertThat(foundOrder.getOrderTypeId()).isEqualTo(orderTypeId);
    }

    @Test
    @DisplayName("Should find orders by current status")
    void shouldFindOrdersByCurrentStatus() {
        // Given
        UUID orderTypeId = UUID.randomUUID();
        Order order1 = new Order(UUID.randomUUID(), "ORD-001", LocalDate.now(), orderTypeId);
        Order order2 = new Order(UUID.randomUUID(), "ORD-002", LocalDate.now(), orderTypeId);
        
        orderRepository.save(order1);
        orderRepository.save(order2);
        entityManager.flush();

        // When
        List<Order> createdOrders = orderRepository.findByCurrentStatus("CREATED");

        // Then
        assertThat(createdOrders).hasSize(2);
        assertThat(createdOrders).extracting(Order::getOrderIdentifier)
            .containsExactlyInAnyOrder("ORD-001", "ORD-002");
    }
}