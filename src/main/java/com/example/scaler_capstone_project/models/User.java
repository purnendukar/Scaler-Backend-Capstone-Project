package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class User extends BaseModel{
    private String name;
    private String email;
    private String password;
}
