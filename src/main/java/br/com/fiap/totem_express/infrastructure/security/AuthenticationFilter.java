package br.com.fiap.totem_express.infrastructure.security;

import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//TODO: teste
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN = "Bearer ";

    private final JWTService service;
    private final UserDetailsServiceImpl repository;

    public AuthenticationFilter(JWTService service, UserDetailsServiceImpl repository) {
        this.service = service;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        findAuthenticationHeader(request).map(this::extractToken).flatMap(service::getUserFromToken).ifPresent(this::authenticate);
        chain.doFilter(request, response);
    }

    private void authenticate(Long id){
        final var user = repository.loadUserById(id);
        final var authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Optional<String> findAuthenticationHeader(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(AUTHENTICATION_HEADER));
    }

    private String extractToken(String token){
        return token.startsWith(BEARER_TOKEN)
                ? token.substring(BEARER_TOKEN.length())
                : null;
    }
}
