package com.example.scaler_capstone_project.repositories;

import com.example.scaler_capstone_project.models.Show;
import com.example.scaler_capstone_project.models.ShowSeatType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowSeatTypeRepository extends JpaRepository<ShowSeatType, Long> {
    List<ShowSeatType> findAllByShow(Show show);
}
