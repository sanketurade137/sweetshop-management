package com.example.SweetshopManagementSystem.CustomException;

public class ProductIsNotFound extends Exception{
    public ProductIsNotFound(String msg){
        super(msg);
    }

}
