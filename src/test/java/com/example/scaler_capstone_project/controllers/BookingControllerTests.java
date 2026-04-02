package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.BookingRequestDTO;
import com.example.scaler_capstone_project.dtos.BookingResponseDTO;
import com.example.scaler_capstone_project.dtos.ResponseStatus;
import com.example.scaler_capstone_project.models.Ticket;
import com.example.scaler_capstone_project.services.IBookingService;
import com.example.scaler_capstone_project.services.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IBookingService bookingService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBookMovie_Success() throws Exception {
        // Arrange
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setShowId(1L);
        requestDTO.setShowSeatsIds(Arrays.asList(1L, 2L));

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setAmount(500L);

        String validToken = "Bearer valid-jwt-token";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(bookingService.bookMovie(1L, 1L, Arrays.asList(1L, 2L))).thenReturn(ticket);

        // Act & Assert
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", validToken)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("SUCCESS"))
                .andExpect(jsonPath("$.ticket.id").value(1))
                .andExpect(jsonPath("$.ticket.amount").value(500));
    }

    @Test
    void testBookMovie_MissingToken() throws Exception {
        // Arrange
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setShowId(1L);
        requestDTO.setShowSeatsIds(Arrays.asList(1L, 2L));

        // Act & Assert
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.ticket").doesNotExist());
    }

    @Test
    void testBookMovie_InvalidToken() throws Exception {
        // Arrange
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setShowId(1L);
        requestDTO.setShowSeatsIds(Arrays.asList(1L, 2L));

        String invalidToken = "Bearer invalid-token";

        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", invalidToken)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.ticket").doesNotExist());
    }

    @Test
    void testBookMovie_Failure() throws Exception {
        // Arrange
        BookingRequestDTO requestDTO = new BookingRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setShowId(1L);
        requestDTO.setShowSeatsIds(Arrays.asList(1L, 2L));

        String validToken = "Bearer valid-jwt-token";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(bookingService.bookMovie(any(Long.class), any(Long.class), anyList()))
                .thenThrow(new RuntimeException("Booking failed"));

        // Act & Assert
        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", validToken)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseStatus").value("FAILURE"))
                .andExpect(jsonPath("$.ticket").doesNotExist());
    }
}
