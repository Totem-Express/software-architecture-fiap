package br.com.fiap.totem_express.infrastructure.security;

import br.com.fiap.totem_express.domain.user.User;
import br.com.fiap.totem_express.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Gloria Maria", "gloriamaria@email.com", "114.974.750-15", LocalDateTime.now(), Role.USER);
        userDetails = new UserDetailsImpl(user);
    }

    @Test
    void should_return_authorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next()).isEqualTo(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    void should_return_empty_password() {
        assertThat(userDetails.getPassword()).isEmpty();
    }

    @Test
    void should_return_username() {
        assertThat(userDetails.getUsername()).isEqualTo("114.974.750-15");
    }

    @Test
    void should_return_account_non_expired() {
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void should_return_account_non_locked() {
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void should_return_credentials_non_expired() {
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void should_return_enabled() {
        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void should_return_user() {
        assertThat(userDetails.user()).isEqualTo(user);
    }
}