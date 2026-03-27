package com.example.scaler_capstone_project.repositories;

import com.example.scaler_capstone_project.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
}
