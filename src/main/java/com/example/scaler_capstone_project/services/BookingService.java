package com.example.scaler_capstone_project.services;


import com.example.scaler_capstone_project.exceptions.SeatsNoLongerAvailableException;
import com.example.scaler_capstone_project.exceptions.ShowNotFoundException;
import com.example.scaler_capstone_project.exceptions.UserNotFoundException;
import com.example.scaler_capstone_project.models.*;
import com.example.scaler_capstone_project.repositories.ShowRepository;
import com.example.scaler_capstone_project.repositories.ShowSeatRepository;
import com.example.scaler_capstone_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class BookingService implements IBookingService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;
    @Autowired
    private PriceCalculation priceCalculation;

    public Ticket bookMovie(Long userId, Long showId, List<Long> showSeatIds){ //4,5,6 => 4,5
        /*
        1st step validate the user
         */

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            //user is not a valid user
            //redirect the user to signup/ login flow
            throw new UserNotFoundException("User with userId " + userId + " is not valid");
        }

        User user = optionalUser.get();

        Optional<Show> optionalShow = showRepository.findById(showId);

        if(optionalShow.isEmpty()){
            throw new ShowNotFoundException("Show is invalid");
        }

        Show show = optionalShow.get();

        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatIds);

        if(showSeats.size() != showSeatIds.size()){
            //some mismatch of seatIds in the db, invalid seatIds
            throw new RuntimeException("Please select valid seats and proceed");
        }


        for(ShowSeat showSeat: showSeats){
            if(!showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)){
                throw new SeatsNoLongerAvailableException("Seats you selected are no longer available");
            }
        }

        /*
        userId - User
        showId  - Show
        showSeats are available - ShowSeats
         */


        /*
        Block the seats and then proceed with the payment
        Take the lock on the db over here using serializable isolation level
        HW - put the line 81 to 84 under a transaction session?
        u1, u2
         */


        //10 users are waiting here
        //u1- {1,2} - blocked
        //u2 - {2,3}
        for(ShowSeat showSeat: showSeats){
            if(showSeat.getShowSeatStatus().equals(ShowSeatStatus.AVAILABLE)) {
                showSeat.setShowSeatStatus(ShowSeatStatus.BLOCKED);
                showSeatRepository.save(showSeat);
            }else{
                throw new RuntimeException();
            }
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTicketStatus(TicketStatus.PENDING);
        ticket.setShowSeats(showSeats);
        ticket.setAmount(priceCalculation.calculatePrice(showSeats,show));

        //start the payment flow

        //once the payment is done, or it failed
        //call the payment service
        //come back and change the seat status
        //if payment failed => change all seat status to available
        //else change to booked




        return ticket;
    }
}
