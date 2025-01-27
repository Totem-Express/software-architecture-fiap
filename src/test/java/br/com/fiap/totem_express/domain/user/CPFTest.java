package br.com.fiap.totem_express.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CPFTest {

    @Test
    void formatted_should_return_formatted_cpf_when_value_is_valid() {
        CPF cpf = new CPF("12345678909");
        String formattedCpf = cpf.formatted();
        assertThat(formattedCpf).isEqualTo("123.456.789-09");
    }

    @Test
    void formatted_should_return_empty_when_value_is_empty() {
        CPF cpf = new CPF("");
        String formattedCpf = cpf.formatted();
        assertThat(formattedCpf).isEqualTo("");
    }

    @Test
    void formatted_should_return_unformatted_value_when_length_is_invalid() {
        CPF cpf = new CPF("12345");
        String formattedCpf = cpf.formatted();
        assertThat(formattedCpf).isEqualTo("12345");
    }

    @Test
    void formatted_should_return_unformatted_value_when_contains_non_numeric_characters() {
        CPF cpf = new CPF("123ABC!@#");
        String formattedCpf = cpf.formatted();
        assertThat(formattedCpf).isEqualTo("123ABC!@#");
    }
}