package com.common.enums;

public enum BookingStatus {
    PENDING, // — Розглядається
    CANCELLED_BY_CLIENT,
    WAITING_FOR_CLIENT_RESPONSE, // — Очікує відповіді від клієнта (наприклад: немає місць на обраний час,
                                 // потрібне підтвердження альтернативи)

    CONFIRMED, // — Підтверджено
    COMPLETED,

    EXPIRED // - Автоматично скасовано (клієнт не відповів вчасно)
}
