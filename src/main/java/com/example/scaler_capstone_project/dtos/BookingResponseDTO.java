package com.example.scaler_capstone_project.dtos;

import com.example.scaler_capstone_project.models.Ticket;
import lombok.Data;

@Data
public class BookingResponseDTO {
    private Ticket ticket;
    private ResponseStatus responseStatus;
}
