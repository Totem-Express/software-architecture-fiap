package br.com.fiap.totem_express.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JWTServiceTest {

    @Test
    void should_return_empty_if_secret_does_not_match() {
        JWTService secret = new JWTService("secret");
        String token = JWT.create().withClaim("user", 42L).sign(Algorithm.HMAC256("anothersecret"));
        assertThat(secret.getUserFromToken(token)).isEmpty();
    }

    @Test
    void should_return_user_id() {
        JWTService secret = new JWTService("secret");
        String token = JWT.create().withClaim("user", 42L).sign(Algorithm.HMAC256("secret"));
        assertThat(secret.getUserFromToken(token)).contains(42L);
    }

    @Test
    void should_return_empty_if_cant_decode() {
        JWTService secret = new JWTService("secret");
        assertThat(secret.getUserFromToken("token")).isEmpty();
    }
}