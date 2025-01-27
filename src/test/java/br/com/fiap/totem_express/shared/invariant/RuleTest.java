package br.com.fiap.totem_express.shared.invariant;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;


public class RuleTest {

    @Test
    void notBlank_should_return_true_for_non_blank_string() {
        Rule<String> rule = Rule.notBlank("String cannot be blank");
        assertThat(rule.validate("Hello")).isTrue();
    }

    @Test
    void notBlank_should_return_false_for_blank_string() {
        Rule<String> rule = Rule.notBlank("String cannot be blank");
        assertThat(rule.validate("")).isFalse();
        assertThat(rule.validate(null)).isFalse();
    }

    @Test
    void notEmpty_should_return_true_for_non_empty_collection() {
        Rule<Collection> rule = Rule.notEmpty("Collection cannot be empty");
        assertThat(rule.validate(List.of(1, 2, 3))).isTrue();
    }

    @Test
    void notEmpty_should_return_false_for_empty_collection() {
        Rule<Collection> rule = Rule.notEmpty("Collection cannot be empty");
        assertThat(rule.validate(List.of())).isFalse();
    }

    @Test
    void notNull_should_return_true_for_non_null_value() {
        Rule<Object> rule = Rule.notNull("Object cannot be null");
        assertThat(rule.validate(new Object())).isTrue();
    }

    @Test
    void notNull_should_return_false_for_null_value() {
        Rule<Object> rule = Rule.notNull("Object cannot be null");
        assertThat(rule.validate(null)).isFalse();
    }

    @Test
    void gt_should_return_true_for_value_greater_than_target() {
        Rule<BigDecimal> rule = Rule.gt(new BigDecimal("10"), "Value must be greater than 10");
        assertThat(rule.validate(new BigDecimal("11"))).isTrue();
    }

    @Test
    void gt_should_return_false_for_value_not_greater_than_target() {
        Rule<BigDecimal> rule = Rule.gt(new BigDecimal("10"), "Value must be greater than 10");
        assertThat(rule.validate(new BigDecimal("10"))).isFalse();
        assertThat(rule.validate(new BigDecimal("9"))).isFalse();
    }

    @Test
    void len_should_return_true_for_string_with_exact_length() {
        Rule<String> rule = Rule.len(5, "String must be 5 characters long");
        assertThat(rule.validate("Hello")).isTrue();
    }

    @Test
    void len_should_return_false_for_string_with_different_length() {
        Rule<String> rule = Rule.len(5, "String must be 5 characters long");
        assertThat(rule.validate("Hi")).isFalse();
        assertThat(rule.validate("Hello World")).isFalse();
    }

    @Test
    void min_should_return_true_for_string_with_minimum_length() {
        Rule<String> rule = Rule.min(5, "String must be at least 5 characters long");
        assertThat(rule.validate("Hello")).isTrue();
        assertThat(rule.validate("Hello World")).isTrue();
    }

    @Test
    void min_should_return_false_for_string_with_less_than_minimum_length() {
        Rule<String> rule = Rule.min(5, "String must be at least 5 characters long");
        assertThat(rule.validate("Hi")).isFalse();
    }

    @Test
    void max_should_return_true_for_string_with_maximum_length() {
        Rule<String> rule = Rule.max(5, "String must be at most 5 characters long");
        assertThat(rule.validate("Hello")).isTrue();
        assertThat(rule.validate("Hi")).isTrue();
    }

    @Test
    void max_should_return_false_for_string_with_more_than_maximum_length() {
        Rule<String> rule = Rule.max(5, "String must be at most 5 characters long");
        assertThat(rule.validate("Hello World")).isFalse();
    }

    @Test
    void validCPF_should_return_true_for_valid_cpf() {
        Rule<String> rule = Rule.validCPF("Invalid CPF");
        assertThat(rule.validate("52998224725")).isTrue();
    }

    @Test
    void validCPF_should_return_false_for_invalid_cpf() {
        Rule<String> rule = Rule.validCPF("Invalid CPF");
        assertThat(rule.validate("12345678900")).isFalse();
    }
}