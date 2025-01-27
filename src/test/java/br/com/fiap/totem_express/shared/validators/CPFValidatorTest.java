package br.com.fiap.totem_express.shared.validators;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class CPFValidatorTest {

    @Test
    void should_return_false_if_invalid() {
        assertThat(CPFValidator.validate("12345678900")).isFalse();
        assertThat(CPFValidator.validate("12345678900")).isFalse();
        assertThat(CPFValidator.validate("00000000000")).isFalse();
        assertThat(CPFValidator.validate("11111111111")).isFalse();
        assertThat(CPFValidator.validate("22222222222")).isFalse();
        assertThat(CPFValidator.validate("33333333333")).isFalse();
        assertThat(CPFValidator.validate("44444444444")).isFalse();
        assertThat(CPFValidator.validate("55555555555")).isFalse();
        assertThat(CPFValidator.validate("66666666666")).isFalse();
        assertThat(CPFValidator.validate("77777777777")).isFalse();
        assertThat(CPFValidator.validate("88888888888")).isFalse();
        assertThat(CPFValidator.validate("99999999999")).isFalse();
    }

    @Test
    void should_accept_valid_cpfs() {
        assertThat(CPFValidator.validate("52998224725")).isTrue();
        assertThat(CPFValidator.validate("12345678909")).isTrue();
    }

    @Test
    void should_validate_even_using_punctuation() {
        assertThat(CPFValidator.validate("529.982.247-25")).isTrue();
        assertThat(CPFValidator.validate("123.456.789-09")).isTrue();
        assertThat(CPFValidator.validate("529.982.247-24")).isFalse();
        assertThat(CPFValidator.validate("123.456.789-08")).isFalse();
    }
}
