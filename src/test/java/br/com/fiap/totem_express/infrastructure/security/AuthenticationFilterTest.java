package br.com.fiap.totem_express.infrastructure.security;

import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    private JWTService jwtService;

    private UserDetailsServiceImpl userDetailsService;

    private AuthenticationFilter authenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        jwtService = mock(JWTService.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authenticationFilter = new AuthenticationFilter(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_authenticate_user_when_token_is_valid() throws ServletException, IOException {
        String token = "Bearer validToken";
        Long userId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        request.addHeader("Authorization", token);
        when(jwtService.getUserFromToken("validToken")).thenReturn(Optional.of(userId));
        when(userDetailsService.loadUserById(userId)).thenReturn(userDetails);

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).getUserFromToken("validToken");
        verify(userDetailsService).loadUserById(userId);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void should_not_authenticate_user_when_token_is_invalid() throws ServletException, IOException {
        String token = "Bearer invalidToken";

        request.addHeader("Authorization", token);
        when(jwtService.getUserFromToken("invalidToken")).thenReturn(Optional.empty());

        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).getUserFromToken("invalidToken");
        verify(userDetailsService, never()).loadUserById(anyLong());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void should_not_authenticate_user_when_no_token_is_provided() throws ServletException, IOException {
        authenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService, never()).getUserFromToken(anyString());
        verify(userDetailsService, never()).loadUserById(anyLong());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}