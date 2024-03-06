package com.decouvrezvotreville.apispring.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeHttpRequest->{
                authorizeHttpRequest
                        .requestMatchers("/api/v1/auth/**").permitAll();
            authorizeHttpRequest
                    .requestMatchers("/swagger-ui/**").permitAll();
            authorizeHttpRequest
                    .requestMatchers("/swagger-ui.html").permitAll();
            authorizeHttpRequest
                    .requestMatchers("/v3/api-docs/**").permitAll();


                authorizeHttpRequest.requestMatchers("/error").permitAll();
                authorizeHttpRequest.anyRequest().authenticated();
        });


        http.sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
                ));

        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("*"));
            configuration.setAllowedHeaders(Arrays.asList("*"));
            return configuration;
        }));
       // http.cors(Customizer.withDefaults());
        return http.build();
    }




}
