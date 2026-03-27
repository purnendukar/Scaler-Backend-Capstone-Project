package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Ticket extends BaseModel{
    private String no;

    private TicketStatus ticketStatus;

    @ManyToOne
    private User user;

    private Long amount;

    @ManyToMany
    private List<ShowSeat> showSeats;

    @OneToMany
    private List<Payment> payments;
}
