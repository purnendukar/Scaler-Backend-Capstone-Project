package com.example.scaler_capstone_project.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDTO {
    private Long userId;
    private List<Long> showSeatsIds;
    private Long showId;
}
