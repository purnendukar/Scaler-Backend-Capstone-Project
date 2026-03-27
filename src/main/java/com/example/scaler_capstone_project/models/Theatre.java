package com.example.scaler_capstone_project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Theatre extends BaseModel{
    private String name;
    @OneToMany
    private List<Screen> screens;
    @ManyToOne
    private City city;
}
