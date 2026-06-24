package org.example.springlogin.service;

import org.example.springlogin.model.User;
import org.example.springlogin.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl {

    private final UserRepository userRepository;

    public  UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByName(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUserName(username).
                orElseThrow(()->new UsernameNotFoundException("User not found: "+username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }



}
