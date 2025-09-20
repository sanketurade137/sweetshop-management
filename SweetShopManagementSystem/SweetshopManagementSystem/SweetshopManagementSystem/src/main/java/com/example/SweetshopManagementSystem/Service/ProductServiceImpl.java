package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.CustomException.ProductIsNotFound;
import com.example.SweetshopManagementSystem.Entity.Product;
import com.example.SweetshopManagementSystem.Repository.OrderRepository;
import com.example.SweetshopManagementSystem.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductServiceImpl(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getById(long id) {
        return  productRepository.findById(id);
    }

    @Override
    public Product add(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(long id, Product updateProduct) throws ProductIsNotFound{
        Product existing =productRepository.findById(id).orElseThrow(()->
                new ProductIsNotFound("product is not available"));
        existing.setName(updateProduct.getName());
        existing.setStock(updateProduct.getStock());
        existing.setCatagory(updateProduct.getCatagory());
        existing.setPrice(updateProduct.getPrice());
        existing.setImageurl(updateProduct.getImageurl());
        return productRepository.save(existing);
    }

    @Override
    public void delete(long id) {
        orderRepository.removeProductFromOrders(id); // remove from orders first
        productRepository.deleteById(id);
    }

    @Override
    public Product restock(long id, int quantity) throws Exception{
        Product product=productRepository.findById(id).orElseThrow(()->
                new ProductIsNotFound("product is not available"));
        product.setStock(product.getStock()+quantity);


        return productRepository.save(product);
    }

    @Override
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> searchByPrice(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
