package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.LoginRequestDTO;
import com.example.scaler_capstone_project.dtos.LoginResponseDTO;
import com.example.scaler_capstone_project.dtos.SignUpRequestDTO;
import com.example.scaler_capstone_project.dtos.SignUpResponseDTO;
import com.example.scaler_capstone_project.models.User;
import com.example.scaler_capstone_project.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignup_Success() throws Exception {
        // Arrange
        SignUpRequestDTO requestDTO = new SignUpRequestDTO();
        requestDTO.setName("New User");
        requestDTO.setEmail("new@example.com");
        requestDTO.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setName("New User");
        user.setEmail("new@example.com");

        when(userService.signup("New User", "new@example.com", "password123")).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.name").value("New User"))
                .andExpect(jsonPath("$.user.email").value("new@example.com"));
    }

    @Test
    void testSignup_Failure() throws Exception {
        // Arrange
        SignUpRequestDTO requestDTO = new SignUpRequestDTO();
        requestDTO.setName("Existing User");
        requestDTO.setEmail("existing@example.com");
        requestDTO.setPassword("password123");

        when(userService.signup(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Email already registered"));

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("FAILURE"))
                .andExpect(jsonPath("$.user").doesNotExist());
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("existing@example.com");
        requestDTO.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setName("Existing User");
        user.setEmail("existing@example.com");

        Map<String, Object> loginResult = Map.of("user", user, "token", "mocked-jwt-token");

        when(userService.login("existing@example.com", "password123")).thenReturn(loginResult);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.user.id").value(1))
                .andExpect(jsonPath("$.user.name").value("Existing User"))
                .andExpect(jsonPath("$.user.email").value("existing@example.com"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    void testLogin_Failure() throws Exception {
        // Arrange
        LoginRequestDTO requestDTO = new LoginRequestDTO();
        requestDTO.setEmail("nonexistent@example.com");
        requestDTO.setPassword("password123");

        when(userService.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("FAILURE"))
                .andExpect(jsonPath("$.user").doesNotExist());
    }
}
