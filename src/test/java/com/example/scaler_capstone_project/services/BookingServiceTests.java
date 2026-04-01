package com.example.scaler_capstone_project.services;

import com.example.scaler_capstone_project.exceptions.SeatsNoLongerAvailableException;
import com.example.scaler_capstone_project.exceptions.ShowNotFoundException;
import com.example.scaler_capstone_project.exceptions.UserNotFoundException;
import com.example.scaler_capstone_project.models.*;
import com.example.scaler_capstone_project.repositories.ShowRepository;
import com.example.scaler_capstone_project.repositories.ShowSeatRepository;
import com.example.scaler_capstone_project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private ShowSeatRepository showSeatRepository;

    @Mock
    private PriceCalculation priceCalculation;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Show show;
    private ShowSeat showSeat1;
    private ShowSeat showSeat2;
    private List<Long> showSeatIds;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        show = new Show();
        show.setId(1L);

        showSeat1 = new ShowSeat();
        showSeat1.setId(1L);
        showSeat1.setShowSeatStatus(ShowSeatStatus.AVAILABLE);

        showSeat2 = new ShowSeat();
        showSeat2.setId(2L);
        showSeat2.setShowSeatStatus(ShowSeatStatus.AVAILABLE);

        showSeatIds = Arrays.asList(1L, 2L);
    }

    @Test
    void testBookMovie_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(Arrays.asList(showSeat1, showSeat2));
        when(priceCalculation.calculatePrice(any(), any())).thenReturn(100L);

        // Act
        Ticket ticket = bookingService.bookMovie(1L, 1L, showSeatIds);

        // Assert
        assertNotNull(ticket);
        assertEquals(TicketStatus.PENDING, ticket.getTicketStatus());
        assertEquals(user, ticket.getUser());
        assertEquals(100L, ticket.getAmount());
        assertEquals(Arrays.asList(showSeat1, showSeat2), ticket.getShowSeats());

        // Verify seats are blocked
        verify(showSeatRepository, times(2)).save(any(ShowSeat.class));
        assertEquals(ShowSeatStatus.BLOCKED, showSeat1.getShowSeatStatus());
        assertEquals(ShowSeatStatus.BLOCKED, showSeat2.getShowSeatStatus());
    }

    @Test
    void testBookMovie_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
            () -> bookingService.bookMovie(1L, 1L, showSeatIds));
        assertEquals("User with userId 1 is not valid", exception.getMessage());
    }

    @Test
    void testBookMovie_ShowNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ShowNotFoundException exception = assertThrows(ShowNotFoundException.class,
            () -> bookingService.bookMovie(1L, 1L, showSeatIds));
        assertEquals("Show is invalid", exception.getMessage());
    }

    @Test
    void testBookMovie_InvalidSeats() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(Arrays.asList(showSeat1)); // Only one seat found

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookingService.bookMovie(1L, 1L, showSeatIds));
        assertEquals("Please select valid seats and proceed", exception.getMessage());
    }

    @Test
    void testBookMovie_SeatsNotAvailable() {
        // Arrange
        showSeat1.setShowSeatStatus(ShowSeatStatus.BOOKED);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(showRepository.findById(1L)).thenReturn(Optional.of(show));
        when(showSeatRepository.findAllById(showSeatIds)).thenReturn(Arrays.asList(showSeat1, showSeat2));

        // Act & Assert
        SeatsNoLongerAvailableException exception = assertThrows(SeatsNoLongerAvailableException.class,
            () -> bookingService.bookMovie(1L, 1L, showSeatIds));
        assertEquals("Seats you selected are no longer available", exception.getMessage());
    }
}
