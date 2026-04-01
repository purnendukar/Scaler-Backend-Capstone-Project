package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.ResponseStatus;
import com.example.scaler_capstone_project.dtos.SignUpRequestDTO;
import com.example.scaler_capstone_project.dtos.SignUpResponseDTO;
import com.example.scaler_capstone_project.models.User;
import com.example.scaler_capstone_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //localhost:8080/user/signup
    @PostMapping("/user/signup")
    public SignUpResponseDTO signup(@RequestBody SignUpRequestDTO signUpRequestDTO){
        SignUpResponseDTO signupResponseDTO = new SignUpResponseDTO();
        try{
            User user = userService.signup(signUpRequestDTO.getName(),
                    signUpRequestDTO.getEmail(),
                    signUpRequestDTO.getPassword());

            signupResponseDTO.setUser(user);
            signupResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        }catch (Exception ex){
            signupResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }

        return signupResponseDTO;
    }
}
