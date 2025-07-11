package org.erp_microservices.peopleandorganizations.service;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderItem;
import org.erp_microservices.peopleandorganizations.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public Order createOrder(String orderIdentifier, LocalDate orderDate, UUID orderTypeId) {
        Order order = new Order(UUID.randomUUID(), orderIdentifier, orderDate, orderTypeId);
        return orderRepository.save(order);
    }
    
    public Order createSalesOrder(String orderIdentifier, LocalDate orderDate, 
                                  UUID customerId, UUID billingLocationId) {
        UUID salesOrderTypeId = UUID.randomUUID(); // In real implementation, this would be looked up
        Order order = new Order(UUID.randomUUID(), orderIdentifier, orderDate, salesOrderTypeId);
        order.setPlacedByPartyRoleId(customerId);
        order.setBillingLocationContactMechanismId(billingLocationId);
        return orderRepository.save(order);
    }
    
    public Order createPurchaseOrder(String orderIdentifier, LocalDate orderDate, 
                                     UUID supplierId, UUID deliveryLocationId) {
        UUID purchaseOrderTypeId = UUID.randomUUID(); // In real implementation, this would be looked up
        Order order = new Order(UUID.randomUUID(), orderIdentifier, orderDate, purchaseOrderTypeId);
        order.setTakenByPartyRoleId(supplierId);
        order.setTakenViaContactMechanismId(deliveryLocationId);
        return orderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public Optional<Order> findById(UUID id) {
        return orderRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Order> findByOrderIdentifier(String orderIdentifier) {
        return orderRepository.findByOrderIdentifier(orderIdentifier);
    }
    
    @Transactional(readOnly = true)
    public List<Order> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    @Transactional(readOnly = true)
    public List<Order> findByCustomer(UUID customerId) {
        return orderRepository.findByCustomer(customerId);
    }
    
    @Transactional(readOnly = true)
    public List<Order> findBySupplier(UUID supplierId) {
        return orderRepository.findBySupplier(supplierId);
    }
    
    @Transactional(readOnly = true)
    public List<Order> findByStatus(String status) {
        return orderRepository.findByCurrentStatus(status);
    }
    
    public OrderItem addOrderItem(UUID orderId, Long sequenceId, Long quantity, BigDecimal unitPrice, 
                                  UUID productId, LocalDate deliveryDate) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        OrderItem item = order.addItem(sequenceId, quantity, unitPrice, productId);
        item.setEstimatedDeliveryDate(deliveryDate);
        
        orderRepository.save(order);
        return item;
    }
    
    public Order updateOrderItemQuantity(UUID orderId, Long sequenceId, Long newQuantity) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        OrderItem item = order.getItems().stream()
            .filter(i -> i.getSequenceId().equals(sequenceId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Order item not found with sequence: " + sequenceId));
        
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        item.setQuantity(newQuantity);
        return orderRepository.save(order);
    }
    
    public Order removeOrderItem(UUID orderId, Long sequenceId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        order.getItems().removeIf(item -> item.getSequenceId().equals(sequenceId));
        
        return orderRepository.save(order);
    }
    
    public Order updateOrderStatus(UUID orderId, UUID statusTypeId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        order.updateStatus(statusTypeId);
        return orderRepository.save(order);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal calculateOrderTotal(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        
        return order.calculateTotal();
    }
}