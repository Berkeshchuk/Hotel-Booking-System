package com.demo_hotel_service.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo_hotel_service.data.models.hotel_offerings.abstraction.ServiceUnit;

public interface ServiceUnitRepository extends JpaRepository<ServiceUnit, Long> {
    
    @Query( "SELECT s_u FROM ServiceUnit s_u WHERE TYPE(s_u) = :clazz")
    public List<ServiceUnit> findByType(@Param("clazz") Class<? extends ServiceUnit> clazz, Pageable pageable);
}
