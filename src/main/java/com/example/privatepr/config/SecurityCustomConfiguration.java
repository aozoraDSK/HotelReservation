package com.example.privatepr.config;

import com.example.privatepr.config.security.CustomConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityCustomConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/books/all").hasRole("ADMIN")
                        .requestMatchers("/books/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/client/role/").hasRole("ADMIN")
                        .requestMatchers("/client/allRoles/**").hasRole("ADMIN")
                        .requestMatchers("/client/all").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/client/new").permitAll()
                        .requestMatchers("/client/*/edit").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/client/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/hotel/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/hotel/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/hotel/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/hotel/*/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/hotel/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/review/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PATCH, "/review/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/review/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/review/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/rooms/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/rooms/**").hasRole("ADMIN")
                        .requestMatchers("/rooms/roomsForGivenDate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rooms/**").permitAll()
                        .requestMatchers("/auth/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/**").permitAll()
                        // Разрешить доступ к статическим ресурсам (CSS, изображениям)
                        .requestMatchers(
                                "/register",
                                "/login",
                                "/css/**",
                                "/css/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .accessDeniedPage("/auth/accessDenied"))
                .formLogin(fromLogin -> fromLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .failureUrl("/auth/failed")
                        .defaultSuccessUrl("/hotel/all", true)
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                );
        http.cors(configurer -> configurer.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.apply(new CustomConfigurer<>());
        http.logout(configurer -> configurer.logoutSuccessUrl("/auth/success"));
        return http.build();
    }
}
