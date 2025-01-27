package br.com.fiap.totem_express.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigurationTest {

    private AuthenticationFilter authenticationFilter;

    private SecurityConfiguration securityConfiguration;

    @BeforeEach
    void setUp() {
        authenticationFilter = mock(AuthenticationFilter.class);
        securityConfiguration = new SecurityConfiguration(authenticationFilter);
    }

    @Test
    void should_build_authentication_manager() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = securityConfiguration.handleBuildAuthenticationManager(configuration);

        assertThat(result).isNotNull();
    }

    @Test
    void should_select_password_encoder() {
        PasswordEncoder passwordEncoder = securityConfiguration.handleSelectPasswordEncoder();

        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }
}