package com.demo_resource_service.data.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WorkerSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private SpaWorker worker;

    private LocalDate date; // Конкретна дата зміни
    private LocalDateTime startTime; // Початок зміни (наприклад, 2025-05-12 09:00)
    private LocalDateTime endTime;   // Кінець зміни (наприклад, 2025-05-12 18:00)
}
