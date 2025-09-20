package com.example.SweetshopManagementSystem.Service;

import com.example.SweetshopManagementSystem.Entity.User;
import com.example.SweetshopManagementSystem.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, JwtUtilService jwtUtilService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean register(User user) {
        if(userRepository.findByUserName(user.getUserName()).isPresent()){
            throw new RuntimeException("user name already exist");
        }
        User x=userRepository.save(user);
        if(x==null) {
            return false;
        }
        return true;
    }

    @Override
    public String login(User user) {
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
        if(authenticate.isAuthenticated()) {
            return jwtUtilService.generateToken(user);
        }
        else{
            return "invalid credential";
        }
    }

    @Override
    public boolean delete(long id, User user) {
        boolean flage=false;
        try{
            userRepository.deleteById(id);
            flage=true;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return flage;
    }
}
