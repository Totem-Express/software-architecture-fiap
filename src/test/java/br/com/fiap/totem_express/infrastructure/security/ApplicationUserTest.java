package br.com.fiap.totem_express.infrastructure.security;

import br.com.fiap.totem_express.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.fiap.totem_express.domain.user.Role.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ApplicationUserTest {

    private SecurityContext securityContext;
    private Authentication authentication;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        userDetails = mock(UserDetailsImpl.class);
    }

    @Test
    void should_return_empty_when_no_authentication() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            Optional<User> result = ApplicationUser.retrieve();

            assertThat(result).isEmpty();
        }
    }

    @Test
    void should_return_empty_when_authentication_is_not_instance_of_UsernamePasswordAuthenticationToken() {
        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            Optional<User> result = ApplicationUser.retrieve();

            assertThat(result).isEmpty();
        }
    }

    @Test
    void should_return_user_when_authentication_is_valid() {
        User user = new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), ADMIN);
        when(userDetails.user()).thenReturn(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, null);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(auth);

            Optional<User> result = ApplicationUser.retrieve();

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(user);
        }
    }

    @Test
    void should_return_user_id_when_authentication_is_valid() {
        User user = new User(1L, "Brad Pitt", "bradpitt@email.com", "114.974.750-15", LocalDateTime.now(), ADMIN);
        when(userDetails.user()).thenReturn(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, null);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(auth);

            Optional<Long> result = ApplicationUser.retrieveUserId();

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(user.getId());
        }
    }
}