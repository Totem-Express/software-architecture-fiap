package br.com.fiap.totem_express.shared.invariant;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class InvariantTest {

    @Test
    void should_throw_exception_if_rule_does_not_match() {
        Rule<String> notBlankRule = Rule.notBlank("Value cannot be blank");
        String target = "";

        assertThatThrownBy(() -> Invariant.of(target, notBlankRule))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Value cannot be blank");
    }

    @Test
    void should_not_throw_exception_when_all_rules_valid() {
        Rule<String> notBlankRule = Rule.notBlank("Value cannot be blank");
        Rule<String> minLengthRule = Rule.min(3, "Value must have at least 3 characters");
        String target = "Valid";

        assertThatCode(() -> Invariant.of(target, notBlankRule, minLengthRule))
                .doesNotThrowAnyException();
    }

    @Test
    void should_throw_exception_when_any_rule_not_valid() {
        Rule<String> notBlankRule = Rule.notBlank("Value cannot be blank");
        Rule<String> minLengthRule = Rule.min(3, "Value must have at least 3 characters");
        String target = "No";

        assertThatThrownBy(() -> Invariant.of(target, notBlankRule, minLengthRule))
                .isInstanceOf(InvariantException.class)
                .hasMessageContaining("Value must have at least 3 characters");
    }
}