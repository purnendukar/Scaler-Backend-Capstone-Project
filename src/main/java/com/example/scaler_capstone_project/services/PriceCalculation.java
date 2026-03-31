package com.example.scaler_capstone_project.services;


import com.example.scaler_capstone_project.models.SeatType;
import com.example.scaler_capstone_project.models.Show;
import com.example.scaler_capstone_project.models.ShowSeat;
import com.example.scaler_capstone_project.models.ShowSeatType;
import com.example.scaler_capstone_project.repositories.ShowSeatTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculation implements IPriceCalculation {
    @Autowired
    private ShowSeatTypeRepository showSeatTypeRepository;

    public Long calculatePrice(List<ShowSeat> showSeats, Show show){

        /*
        show_seatType table

        Show_id       SeatType_id         price
        1             1              150
        1             2              200
        1             3              300
        2             1              160
        2........
        ...
        ...

         */
        List<ShowSeatType> showSeatTypes = showSeatTypeRepository.findAllByShow(show);

        long price = 0;

        for(ShowSeat showSeat: showSeats){
            //know the type of the seat
            SeatType seatType = showSeat.getSeat().getSeatType();
            //1,showId = 1
            for(ShowSeatType showSeatType: showSeatTypes){
                if(showSeatType.getSeatType().equals(seatType)){
                    price += showSeatType.getPrice();
                    break;
                }
            }
        }

        return price;
    }
}
