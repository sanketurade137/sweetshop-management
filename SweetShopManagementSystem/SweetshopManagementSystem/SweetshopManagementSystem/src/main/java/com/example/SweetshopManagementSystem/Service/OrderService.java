package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.Entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface OrderService {
    List<Order> getAll();
    Optional<Order> getById(long id);
    Order purchase( Order order)throws Exception;

}
