package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Entity
public class Seat extends BaseModel{
    private String no;

    @Enumerated(EnumType.ORDINAL)
    private SeatType seatType;

}
