package com.demo_resource_service.data.models;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;

@Getter
@Setter

@Entity
@Table(name = "physical_rooms")
public class PhysicalRoom {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true) 
    private String roomNumber;

    private int cleaningTimeInMinutes;
    private boolean outOfService;

}
