package org.erp_microservices.peopleandorganizations.graphql;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.erp_microservices.peopleandorganizations.domain.OrderItem;
import org.erp_microservices.peopleandorganizations.service.OrderService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class OrderResolver {
    
    private final OrderService orderService;
    
    public OrderResolver(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @QueryMapping
    public Optional<Order> order(@Argument UUID id) {
        return orderService.findById(id);
    }
    
    @QueryMapping
    public Optional<Order> orderByIdentifier(@Argument String orderIdentifier) {
        return orderService.findByOrderIdentifier(orderIdentifier);
    }
    
    @QueryMapping
    public List<Order> ordersByDateRange(@Argument LocalDate startDate, @Argument LocalDate endDate) {
        return orderService.findByDateRange(startDate, endDate);
    }
    
    @QueryMapping
    public List<Order> ordersByCustomer(@Argument UUID customerId) {
        return orderService.findByCustomer(customerId);
    }
    
    @QueryMapping
    public List<Order> ordersBySupplier(@Argument UUID supplierId) {
        return orderService.findBySupplier(supplierId);
    }
    
    @QueryMapping
    public List<Order> ordersByStatus(@Argument String status) {
        return orderService.findByStatus(status);
    }
    
    @MutationMapping
    public Order createSalesOrder(@Argument CreateSalesOrderInput input) {
        return orderService.createSalesOrder(
            input.getOrderIdentifier(),
            input.getOrderDate(),
            input.getCustomerId(),
            input.getBillingLocationId()
        );
    }
    
    @MutationMapping
    public Order createPurchaseOrder(@Argument CreatePurchaseOrderInput input) {
        return orderService.createPurchaseOrder(
            input.getOrderIdentifier(),
            input.getOrderDate(),
            input.getSupplierId(),
            input.getDeliveryLocationId()
        );
    }
    
    @MutationMapping
    public OrderItem addOrderItem(@Argument AddOrderItemInput input) {
        return orderService.addOrderItem(
            input.getOrderId(),
            input.getSequenceId().longValue(),
            input.getQuantity().longValue(),
            input.getUnitPrice(),
            input.getProductId(),
            input.getDeliveryDate()
        );
    }
    
    @MutationMapping
    public Order updateOrderItemQuantity(@Argument UpdateOrderItemQuantityInput input) {
        return orderService.updateOrderItemQuantity(
            input.getOrderId(),
            input.getSequenceId().longValue(),
            input.getNewQuantity().longValue()
        );
    }
    
    @MutationMapping
    public Order removeOrderItem(@Argument RemoveOrderItemInput input) {
        return orderService.removeOrderItem(
            input.getOrderId(),
            input.getSequenceId().longValue()
        );
    }
    
    @SchemaMapping(typeName = "Order", field = "total")
    public BigDecimal getOrderTotal(Order order) {
        return order.calculateTotal();
    }
    
    @SchemaMapping(typeName = "Order", field = "orderType")
    public String getOrderType(Order order) {
        // In a full implementation, this would look up the order type name
        UUID orderTypeId = order.getOrderTypeId();
        if (orderTypeId != null) {
            // Simplified logic - in reality would query OrderType table
            return "SALES"; // or "PURCHASE" based on actual type
        }
        return null;
    }
    
    @SchemaMapping(typeName = "OrderItem", field = "itemTotal")
    public BigDecimal getItemTotal(OrderItem item) {
        return item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
    
    // Input classes
    public static class CreateSalesOrderInput {
        private String orderIdentifier;
        private LocalDate orderDate;
        private UUID customerId;
        private UUID billingLocationId;
        
        // Getters and setters
        public String getOrderIdentifier() { return orderIdentifier; }
        public void setOrderIdentifier(String orderIdentifier) { this.orderIdentifier = orderIdentifier; }
        
        public LocalDate getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
        
        public UUID getCustomerId() { return customerId; }
        public void setCustomerId(UUID customerId) { this.customerId = customerId; }
        
        public UUID getBillingLocationId() { return billingLocationId; }
        public void setBillingLocationId(UUID billingLocationId) { this.billingLocationId = billingLocationId; }
    }
    
    public static class CreatePurchaseOrderInput {
        private String orderIdentifier;
        private LocalDate orderDate;
        private UUID supplierId;
        private UUID deliveryLocationId;
        
        // Getters and setters
        public String getOrderIdentifier() { return orderIdentifier; }
        public void setOrderIdentifier(String orderIdentifier) { this.orderIdentifier = orderIdentifier; }
        
        public LocalDate getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
        
        public UUID getSupplierId() { return supplierId; }
        public void setSupplierId(UUID supplierId) { this.supplierId = supplierId; }
        
        public UUID getDeliveryLocationId() { return deliveryLocationId; }
        public void setDeliveryLocationId(UUID deliveryLocationId) { this.deliveryLocationId = deliveryLocationId; }
    }
    
    public static class AddOrderItemInput {
        private UUID orderId;
        private Integer sequenceId;
        private Integer quantity;
        private BigDecimal unitPrice;
        private UUID productId;
        private LocalDate deliveryDate;
        
        // Getters and setters
        public UUID getOrderId() { return orderId; }
        public void setOrderId(UUID orderId) { this.orderId = orderId; }
        
        public Integer getSequenceId() { return sequenceId; }
        public void setSequenceId(Integer sequenceId) { this.sequenceId = sequenceId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        
        public UUID getProductId() { return productId; }
        public void setProductId(UUID productId) { this.productId = productId; }
        
        public LocalDate getDeliveryDate() { return deliveryDate; }
        public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    }
    
    public static class UpdateOrderItemQuantityInput {
        private UUID orderId;
        private Integer sequenceId;
        private Integer newQuantity;
        
        // Getters and setters
        public UUID getOrderId() { return orderId; }
        public void setOrderId(UUID orderId) { this.orderId = orderId; }
        
        public Integer getSequenceId() { return sequenceId; }
        public void setSequenceId(Integer sequenceId) { this.sequenceId = sequenceId; }
        
        public Integer getNewQuantity() { return newQuantity; }
        public void setNewQuantity(Integer newQuantity) { this.newQuantity = newQuantity; }
    }
    
    public static class RemoveOrderItemInput {
        private UUID orderId;
        private Integer sequenceId;
        
        // Getters and setters
        public UUID getOrderId() { return orderId; }
        public void setOrderId(UUID orderId) { this.orderId = orderId; }
        
        public Integer getSequenceId() { return sequenceId; }
        public void setSequenceId(Integer sequenceId) { this.sequenceId = sequenceId; }
    }
}