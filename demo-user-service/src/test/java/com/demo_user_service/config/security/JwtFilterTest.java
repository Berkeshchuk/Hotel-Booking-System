package com.demo_user_service.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtFilterTest {
    
    private final String jwtSecretKey = "YmFzZTY0c2VjcmV0a2V5dGhhdGlzYXRsZWFzdDMyYnl0ZXM="; //фейковий секретний ключ
    
    private JwtFilter jwtFilter;
    private MockFilterChain mockFilterChain;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter();
        mockFilterChain = new MockFilterChain();
        
        ReflectionTestUtils.setField(jwtFilter, "jwtSecretKey", jwtSecretKey);
        
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateService_whenTokenIsValid() throws Exception {
        String token = createToken("booking-service");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtFilter.doFilterInternal(request, response, mockFilterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth, "Authentication should not be null");
        assertEquals("booking-service", auth.getPrincipal());
    }

    public String createToken(String serviceName) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));

        return Jwts.builder()
            .claim("role", "SYSTEM_SERVICE")
            .subject(serviceName)
            .issuer("edemium-api-gateway")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 300000))
            .signWith(key)
            .compact();
    }
}