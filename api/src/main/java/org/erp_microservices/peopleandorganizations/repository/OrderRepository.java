package org.erp_microservices.peopleandorganizations.repository;

import org.erp_microservices.peopleandorganizations.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    Optional<Order> findByOrderIdentifier(String orderIdentifier);
    
    List<Order> findByOrderTypeId(UUID orderTypeId);
    
    List<Order> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT o FROM Order o WHERE o.placedByPartyRoleId = :partyRoleId")
    List<Order> findByCustomer(@Param("partyRoleId") UUID partyRoleId);
    
    @Query("SELECT o FROM Order o WHERE o.takenByPartyRoleId = :partyRoleId")
    List<Order> findBySupplier(@Param("partyRoleId") UUID partyRoleId);
    
    @Query("SELECT o FROM Order o JOIN o.statusHistory s WHERE s.status = :status " +
           "AND s.statusChanged = (SELECT MAX(s2.statusChanged) FROM OrderStatus s2 WHERE s2.statusForOrder = o)")
    List<Order> findByCurrentStatus(@Param("status") String status);
}