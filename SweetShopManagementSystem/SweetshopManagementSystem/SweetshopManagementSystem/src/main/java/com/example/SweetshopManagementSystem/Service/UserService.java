package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.Entity.User;

public interface UserService {
    boolean register(User user);
    String login(User user);
    boolean delete(long id,User user);

}
