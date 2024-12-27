package br.com.fiap.totem_express.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//TODO: teste
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationFilter AuthenticationFilter;

    public SecurityConfiguration(br.com.fiap.totem_express.infrastructure.security.AuthenticationFilter authenticationFilter) {
        AuthenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain handleConfigureFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request.requestMatchers("/**").permitAll())
                .addFilterBefore(AuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager handleBuildAuthenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder handleSelectPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
