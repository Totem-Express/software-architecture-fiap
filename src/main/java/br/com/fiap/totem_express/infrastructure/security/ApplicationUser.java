package br.com.fiap.totem_express.infrastructure.security;

import br.com.fiap.totem_express.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ApplicationUser {

    public static Optional<User> retrieve(){
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return Optional.empty();

        if (authentication instanceof UsernamePasswordAuthenticationToken && authentication.getPrincipal() instanceof UserDetailsImpl it){
            return Optional.of(it.user());
        }

        return Optional.empty();
    }

    public static Optional<Long> retrieveUserId(){
        return extract(User::getId);
    }

    public static <T> Optional<T> extract(Function<User, T> mapper){
        return retrieve().map(mapper::apply);
    }
}
