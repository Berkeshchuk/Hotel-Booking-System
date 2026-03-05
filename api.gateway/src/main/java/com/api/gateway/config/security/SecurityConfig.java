package com.api.gateway.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Value("${app.security.remember-me-key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf ->
                csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
            )
            .headers(headers ->
                headers.contentSecurityPolicy(csp ->
                    csp.policyDirectives("""
                        default-src 'self';
                        script-src 'self';
                        style-src 'self';
                        img-src 'self' data:;
                        object-src 'none';
                        base-uri 'none';
                        frame-ancestors 'none';
                        connect-src 'self' http://localhost:8087/api/rooms;
                    """.replace("\n", " "))
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/bookings").authenticated()
                .requestMatchers("/bookings-cart").authenticated()
                .requestMatchers("/bookings-submit").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form ->
                form
                    .loginPage("/auth")
                    .loginProcessingUrl("/sign_in")
                    .successHandler((req, res, auth) -> {
                        String redirect = req.getParameter("redirect");

                        if (redirect == null || redirect.isBlank()) {
                            redirect = "/home";
                        }

                        res.setContentType("application/json");
                        res.getWriter().write("""
                            { "redirect": "%s" }
                        """.formatted(redirect));
                    })
                    .failureHandler((HttpServletRequest req,
                                     HttpServletResponse res,
                                     AuthenticationException ex) -> {

                        String error = "Unknown error occurred";

                        if (ex instanceof BadCredentialsException) {
                            error = "Bad credentials";
                        }

                        ex.printStackTrace();

                        res.setStatus(401);
                        res.setContentType("application/json");
                        res.getWriter()
                           .write("{\"error\":\"%s\"}".formatted(error));
                    })
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .addLogoutHandler((LogoutHandler) rememberMeServices())
                    .deleteCookies("SESSION", "remember-me")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/auth")
            )
            .rememberMe(remember ->
                remember.rememberMeServices(rememberMeServices())
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    res.setStatus(500);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"" + authEx.getMessage() + "\"}");
                })
            );

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        return tokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices() {

        PersistentTokenBasedRememberMeServices services =
            new PersistentTokenBasedRememberMeServices(
                rememberMeKey,
                userDetailsService,
                persistentTokenRepository()
            );

        int thirtyDays = 2_592_000;
        services.setTokenValiditySeconds(thirtyDays);
        services.setAlwaysRemember(true);

        return services;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
