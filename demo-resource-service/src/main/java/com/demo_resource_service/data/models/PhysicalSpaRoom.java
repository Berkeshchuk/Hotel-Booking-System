package com.demo_resource_service.data.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Entity
public class PhysicalSpaRoom {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
    private String spaRoomNumber;
    private int clientCapacity;
    private int cleaningTimeInMinutes;
    private boolean outOfService;
}
