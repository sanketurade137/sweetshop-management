package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.CustomException.ProductIsNotFound;
import com.example.SweetshopManagementSystem.Entity.Order;
import com.example.SweetshopManagementSystem.Entity.Product;
import com.example.SweetshopManagementSystem.Repository.OrderRepository;
import com.example.SweetshopManagementSystem.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getById(long id) {
        return orderRepository.findById(id);
    }

    public Order purchase(Order order) {
        List<Product> managedProducts = new ArrayList<>();

        for (Product p : order.getProducts()) {
            Product product = productRepository.findById(p.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + p.getId()));

            if (product.getStock() < order.getQuantity()) {
                throw new RuntimeException("Not enough stock for: " + product.getName());
            }


            product.setStock(product.getStock() - order.getQuantity());
            productRepository.save(product); // update stock in DB

            managedProducts.add(product);
        }


        order.setProducts(managedProducts);
        order.setOrderDate(LocalDateTime.now());


        return orderRepository.save(order);
    }



}
