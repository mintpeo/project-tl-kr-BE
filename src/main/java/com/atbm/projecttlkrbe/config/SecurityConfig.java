package com.atbm.projecttlkrbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String OAUTH_SUCCESS = "http://localhost:5173/google" ;

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/verify/**", "/oauth2/**").permitAll()
                        .requestMatchers("/assets/**",
                                "/api/lesson-cate/**",
                                "/api/lesson/**",
                                "/api/predict/**",
                                "/api/lesson-cate-route/**",
                                "/api/lesson-route/**",
                                "/api/character/**",
                                "/api/user-lesson-progress/**")
                                .permitAll()
                        .requestMatchers("/api/user/**"
//                                "/api/lesson-cate/**",
//                                "/api/lesson/**",
//                                "/api/predict/**"
                        ).authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth.defaultSuccessUrl(OAUTH_SUCCESS, true))
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl(OAUTH_SUCCESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
