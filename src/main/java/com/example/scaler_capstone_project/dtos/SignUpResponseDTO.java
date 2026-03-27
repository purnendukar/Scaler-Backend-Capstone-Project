package com.example.scaler_capstone_project.dtos;

import com.example.scaler_capstone_project.models.User;
import lombok.Data;

@Data
public class SignUpResponseDTO {
    private User user;
    private ResponseStatus responseStatus;
}
