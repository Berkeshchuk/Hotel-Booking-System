package com.demo_hotel_service.data.models.hotel_offerings.abstraction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.demo_hotel_service.data.models.images.ImageRecord;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "service_units", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "service_unit_id", "facility" })
})
@Inheritance(strategy = InheritanceType.JOINED)
public class ServiceUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 4000, nullable = false)
    private String description;

    @Column(length = 100, nullable = false)
    private String type;

    @Column(nullable = false)
    private boolean hiddenFromClient = true; //приховано для майбутніх бронювань, поточні обслуговуються далі

    @Column(nullable = false)
    private boolean outOfService = true; //послугу неможливо фізично надати(поломка, форс-мажор, послуга - застаріла) на невизначений час

    @Column(nullable = false)
    private int guestCapacity;

    @ElementCollection
    @CollectionTable(name = "service_unit_facilities")
    @Column(name = "facility", length = 255, nullable = false)
    private Set<String> facilities;

    @OneToMany(mappedBy = "serviceUnit", cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    private List<ImageRecord> imageRecords;

    @Version
    private Integer version;

    public ServiceUnit(long id, BigDecimal price, String description, String type,
            boolean hiddenFromClient, boolean outOfService,
            int guestCapacity, Set<String> facilities, List<ImageRecord> imageRecords) {
        this.id = id;
        this.price = price;
        this.description = description;
        this.type = type;
        this.hiddenFromClient = hiddenFromClient;
        this.outOfService = outOfService;
        this.guestCapacity = guestCapacity;
        this.facilities = facilities != null ? new HashSet<>(facilities) : new HashSet<>();
        this.imageRecords = imageRecords;
    }

    public List<ImageRecord> getImageRecords(){
        List<ImageRecord> imageRecordsL = imageRecords != null ? imageRecords : new ArrayList<>();
        return imageRecordsL;
    }


}
