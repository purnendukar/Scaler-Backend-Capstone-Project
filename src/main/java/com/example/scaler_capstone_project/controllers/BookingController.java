package com.example.scaler_capstone_project.controllers;

import com.example.scaler_capstone_project.dtos.BookingRequestDTO;
import com.example.scaler_capstone_project.dtos.BookingResponseDTO;
import com.example.scaler_capstone_project.dtos.ResponseStatus;
import com.example.scaler_capstone_project.models.Ticket;
import com.example.scaler_capstone_project.services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/book")
    public BookingResponseDTO bookMovie(@RequestBody BookingRequestDTO bookingMovieRequestDTO){
        BookingResponseDTO bookingMovieResponseDTO = new BookingResponseDTO();

        try {
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
