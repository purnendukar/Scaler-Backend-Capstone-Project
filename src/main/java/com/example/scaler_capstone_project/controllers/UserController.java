package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.ResponseStatus;
import com.example.scaler_capstone_project.dtos.SignUpRequestDTO;
import com.example.scaler_capstone_project.dtos.SignUpResponseDTO;
import com.example.scaler_capstone_project.models.User;
import com.example.scaler_capstone_project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.scaler_capstone_project.dtos.LoginRequestDTO;
import com.example.scaler_capstone_project.dtos.LoginResponseDTO;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    //localhost:8080/user/signup
    @PostMapping("/signup")
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
    
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        try{
            Map<String, Object> loginResult = userService.login(loginRequestDTO.getEmail(),
                    loginRequestDTO.getPassword());

            User user = (User) loginResult.get("user");
            String token = (String) loginResult.get("token");

            loginResponseDTO.setUser(user);
            loginResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            loginResponseDTO.setToken(token);
        }catch (Exception ex){
            loginResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }

        return loginResponseDTO;
    }
}
