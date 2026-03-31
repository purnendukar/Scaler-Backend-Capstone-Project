package com.example.scaler_capstone_project.services;

import com.example.scaler_capstone_project.models.Ticket;

import java.util.List;

public interface IBookingService {
    public Ticket bookMovie(Long userId, Long showId, List<Long> showSeatIds);
}
