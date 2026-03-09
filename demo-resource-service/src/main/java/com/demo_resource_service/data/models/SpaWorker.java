package com.demo_resource_service.data.models;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "spa_workers")
public class SpaWorker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Базова інформація (для відображення адміністратору)
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;

    // Важливо для СПА: деякі клієнти просять майстра певної статі
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    // Статус ресурсу (щоб алгоритм знав, чи можна його бронювати)
    // Наприклад: ACTIVE, SICK_LEAVE, VACATION, FIRED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkerStatus status;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL)
    private List<WorkerSchedule> workSchedules;
    
    // === КОНТАКТИ (для Адміністратора) ===
    @Column(nullable = false)
    private String phoneNumber;

    @Column(name = "spa_unit_id")
    private Set<Long> competentSpaUnitIds;

}


enum WorkerStatus {
    ACTIVE,
    INACTIVE
}
