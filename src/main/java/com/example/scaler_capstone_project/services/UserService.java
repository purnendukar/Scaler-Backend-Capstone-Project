package com.example.scaler_capstone_project.services;


import com.example.scaler_capstone_project.models.User;
import com.example.scaler_capstone_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signup(String name,
                       String email,
                       String password){

        //validate if the email is already registered

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User savedUser = null;

        if(optionalUser.isPresent()){
            throw new RuntimeException("User with this email " + email + " is already registered");
            //ask the user to login, user with this email is already registered.
        }else{
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            //encrypt the password and store the encyrpted password in the db
            user.setPassword(bCryptPasswordEncoder.encode(password));
            savedUser = userRepository.save(user);
        }

        return savedUser;
    }
}
