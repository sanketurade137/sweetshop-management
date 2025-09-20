package com.example.SweetshopManagementSystem.Repository;

import com.example.SweetshopManagementSystem.Entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM order_products WHERE product_id = :productId", nativeQuery = true)
    void removeProductFromOrders(@Param("productId") Long productId);
}
