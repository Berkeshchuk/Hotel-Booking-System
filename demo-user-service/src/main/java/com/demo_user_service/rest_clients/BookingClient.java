package com.demo_user_service.rest_clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

// @Component
// public class BookingClient {
//     private final RestClient restClient;

//     public BookingClient(@Value("${booking_service_url}") String bookingServiceUrl){
//         this.restClient = RestClient.builder()
//             .baseUrl(bookingServiceUrl)
//             .build();
//     }

//     public void linkBookingsToUser(Long userId, String phoneNumber){
//         restClient.post()
//             .uri(uriBuilder -> uriBuilder
//                 .path("/api/bookings/link-user")
//                 .queryParam("userId", userId)
//                 .queryParam("phoneNumber", phoneNumber)
//                 .build()
//             )
//         .retrieve()
//         .toBodilessEntity();
//     }
// }
