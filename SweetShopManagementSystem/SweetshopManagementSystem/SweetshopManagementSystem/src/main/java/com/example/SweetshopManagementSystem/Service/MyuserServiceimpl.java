package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.Entity.User;
import com.example.SweetshopManagementSystem.Repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyuserServiceimpl implements UserDetailsService {

    private final UserRepository userRepository;

    public MyuserServiceimpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> x = userRepository.findByUserName(username);

        //com.EventApi.EventManagement.Entity.User a = null;
        if (x.isPresent()) {
            var b = x.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(b.getUserName())
                    .password(b.getPassword())
                    .roles(b.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }


}
