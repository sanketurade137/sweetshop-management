package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.Entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface ProductService {
    List<Product> getAll();
    Optional<Product> getById(long id);
    Product add(Product product);
    Product update(long id,Product updateProduct) throws Exception;
    void delete(long id);
    Product restock(long id,int quantity)throws Exception;
    List<Product> searchByName(String name);
    List<Product> searchByPrice(double minPrice, double maxPrice);
}
