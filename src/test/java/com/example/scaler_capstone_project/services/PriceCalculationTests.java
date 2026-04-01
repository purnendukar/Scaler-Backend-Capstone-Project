package com.example.scaler_capstone_project.services;

import com.example.scaler_capstone_project.models.*;
import com.example.scaler_capstone_project.repositories.ShowSeatTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceCalculationTests {

    @Mock
    private ShowSeatTypeRepository showSeatTypeRepository;

    @InjectMocks
    private PriceCalculation priceCalculation;

    private Show show;
    private ShowSeat showSeat1;
    private ShowSeat showSeat2;
    private ShowSeat showSeat3;
    private ShowSeatType showSeatTypeSilver;
    private ShowSeatType showSeatTypeGold;
    private ShowSeatType showSeatTypeRecliner;

    @BeforeEach
    void setUp() {
        show = new Show();
        show.setId(1L);

        Seat seat1 = new Seat();
        seat1.setSeatType(SeatType.SILVER);

        Seat seat2 = new Seat();
        seat2.setSeatType(SeatType.GOLD);

        Seat seat3 = new Seat();
        seat3.setSeatType(SeatType.RECLINER);

        showSeat1 = new ShowSeat();
        showSeat1.setSeat(seat1);

        showSeat2 = new ShowSeat();
        showSeat2.setSeat(seat2);

        showSeat3 = new ShowSeat();
        showSeat3.setSeat(seat3);

        showSeatTypeSilver = new ShowSeatType();
        showSeatTypeSilver.setSeatType(SeatType.SILVER);
        showSeatTypeSilver.setPrice(150L);

        showSeatTypeGold = new ShowSeatType();
        showSeatTypeGold.setSeatType(SeatType.GOLD);
        showSeatTypeGold.setPrice(200L);

        showSeatTypeRecliner = new ShowSeatType();
        showSeatTypeRecliner.setSeatType(SeatType.RECLINER);
        showSeatTypeRecliner.setPrice(300L);
    }

    @Test
    void testCalculatePrice_MultipleSeats() {
        // Arrange
        List<ShowSeat> showSeats = Arrays.asList(showSeat1, showSeat2, showSeat3);
        List<ShowSeatType> showSeatTypes = Arrays.asList(showSeatTypeSilver, showSeatTypeGold, showSeatTypeRecliner);
        when(showSeatTypeRepository.findAllByShow(show)).thenReturn(showSeatTypes);

        // Act
        Long price = priceCalculation.calculatePrice(showSeats, show);

        // Assert
        assertEquals(650L, price); // 150 + 200 + 300
        verify(showSeatTypeRepository, times(1)).findAllByShow(show);
    }

    @Test
    void testCalculatePrice_SingleSeat() {
        // Arrange
        List<ShowSeat> showSeats = Arrays.asList(showSeat1);
        List<ShowSeatType> showSeatTypes = Arrays.asList(showSeatTypeSilver);
        when(showSeatTypeRepository.findAllByShow(show)).thenReturn(showSeatTypes);

        // Act
        Long price = priceCalculation.calculatePrice(showSeats, show);

        // Assert
        assertEquals(150L, price);
    }

    @Test
    void testCalculatePrice_EmptySeats() {
        // Arrange
        List<ShowSeat> showSeats = Collections.emptyList();
        List<ShowSeatType> showSeatTypes = Arrays.asList(showSeatTypeSilver, showSeatTypeGold);
        when(showSeatTypeRepository.findAllByShow(show)).thenReturn(showSeatTypes);

        // Act
        Long price = priceCalculation.calculatePrice(showSeats, show);

        // Assert
        assertEquals(0L, price);
    }

    @Test
    void testCalculatePrice_NoMatchingSeatType() {
        // Arrange
        List<ShowSeat> showSeats = Arrays.asList(showSeat1);
        List<ShowSeatType> showSeatTypes = Arrays.asList(showSeatTypeGold); // No SILVER
        when(showSeatTypeRepository.findAllByShow(show)).thenReturn(showSeatTypes);

        // Act
        Long price = priceCalculation.calculatePrice(showSeats, show);

        // Assert
        assertEquals(0L, price); // No matching type, so price 0 for that seat
    }

    @Test
    void testCalculatePrice_MixedMatching() {
        // Arrange
        List<ShowSeat> showSeats = Arrays.asList(showSeat1, showSeat2);
        List<ShowSeatType> showSeatTypes = Arrays.asList(showSeatTypeSilver); // Only SILVER, not GOLD
        when(showSeatTypeRepository.findAllByShow(show)).thenReturn(showSeatTypes);

        // Act
        Long price = priceCalculation.calculatePrice(showSeats, show);

        // Assert
        assertEquals(150L, price); // Only SILVER matched, GOLD not
    }
}
