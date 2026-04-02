package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.BookingRequestDTO;
import com.example.scaler_capstone_project.dtos.BookingResponseDTO;
import com.example.scaler_capstone_project.dtos.ResponseStatus;
import com.example.scaler_capstone_project.models.Ticket;
import com.example.scaler_capstone_project.services.IBookingService;
import com.example.scaler_capstone_project.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/book")
    public BookingResponseDTO bookMovie(@RequestBody BookingRequestDTO bookingMovieRequestDTO,
                                        @RequestHeader(value = "Authorization", required = false) String token){
        BookingResponseDTO bookingMovieResponseDTO = new BookingResponseDTO();

        try {
            // Check if token is provided
            if (token == null || token.trim().isEmpty()) {
                bookingMovieResponseDTO.setResponseStatus(ResponseStatus.UNAUTHORIZED);
                return bookingMovieResponseDTO;
            }

            // Validate the token
            if (!jwtUtil.validateToken(token)) {
                bookingMovieResponseDTO.setResponseStatus(ResponseStatus.UNAUTHORIZED);
                return bookingMovieResponseDTO;
            }

            // Token is valid, proceed with booking
            Ticket ticket = bookingService.bookMovie(
                bookingMovieRequestDTO.getUserId(),
                bookingMovieRequestDTO.getShowId(),
                bookingMovieRequestDTO.getShowSeatsIds()
            );

            bookingMovieResponseDTO.setTicket(ticket);
            bookingMovieResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception e){
            bookingMovieResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }

        return bookingMovieResponseDTO;
    }
}
