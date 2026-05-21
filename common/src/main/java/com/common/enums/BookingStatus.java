package com.common.enums;

public enum BookingStatus {
    PENDING, // — Розглядається
    CONFIRMED, // — Підтверджено
    COMPLETED,
    CANCELLED_BY_CLIENT,
    EXPIRED, // - Автоматично скасовано (клієнт не відповів вчасно)
    REJECTED
}
