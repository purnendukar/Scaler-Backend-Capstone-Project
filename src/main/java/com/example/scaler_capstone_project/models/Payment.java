package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseModel{
    private Long amount;

    @Enumerated(EnumType.ORDINAL)
    private PaymentMode paymentMode;
    private Long reference_id;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;
}
