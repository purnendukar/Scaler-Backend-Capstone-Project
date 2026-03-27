package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Movie extends BaseModel{
    private String name;

}
