package com.example.scaler_capstone_project.services;

import com.example.scaler_capstone_project.models.Show;
import com.example.scaler_capstone_project.models.ShowSeat;

import java.util.List;

public interface IPriceCalculation {
    public Long calculatePrice(List<ShowSeat> showSeats, Show show);
}
