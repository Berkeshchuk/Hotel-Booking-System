package com.common.dto;

import java.time.LocalDateTime;

import com.common.enums.Gender;

public class AllocationBookingDto {
    private Long id;
    private Long bookingUnitId;
    private Integer clientCount;
    private LocalDateTime start;
    private LocalDateTime end;

    private Gender preferedGender = Gender.FEMALE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingUnitId() {
        return bookingUnitId;
    }

    public void setBookingUnitId(Long bookingUnitId) {
        this.bookingUnitId = bookingUnitId;
    }

    public Integer getClientCount() {
        return clientCount;
    }

    public void setClientCount(Integer clientCount) {
        this.clientCount = clientCount;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Gender getPreferedGender() {
        return preferedGender;
    }

    public void setPreferedGender(Gender preferedGender) {
        this.preferedGender = preferedGender;
    }

}
