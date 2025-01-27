package br.com.fiap.totem_express.shared.validators;

import java.util.Set;
import java.util.stream.IntStream;

public class CPFValidator {
    private static final int CPF_SIZE = 11;
    private static final int FIRST_DIGIT_MAX_INDEX = 9;
    private static final int SECOND_DIGIT_MAX_INDEX = 10;

    private static final Set<String> COMMONS_NOT_VALID_CPF = Set.of(
        "00000000000",
        "11111111111",
        "22222222222",
        "33333333333",
        "44444444444",
        "55555555555",
        "66666666666",
        "77777777777",
        "88888888888",
        "99999999999"
    );

    public static boolean validate(String value){
        final var cleaned = value.replaceAll("\\D", "");

        if (cleaned.length() != CPF_SIZE || COMMONS_NOT_VALID_CPF.contains(cleaned)) return false;

        final var digits = IntStream
                .range(0, CPF_SIZE)
                .map(index -> Character.getNumericValue(cleaned.charAt(index)))
                .toArray();

        return validateFirstDigit(digits);
    }

    private static boolean validateFirstDigit(int[] digits){
        var sum = 0;
        for (var index = 0; index < FIRST_DIGIT_MAX_INDEX; index++){
            sum += ( digits[index] * (10 - index) );
        }

        var remainder = (sum * 10) % 11;
        var check = (remainder == 10) ? 0 : remainder;

        if (check != digits[FIRST_DIGIT_MAX_INDEX]){
            return false;
        }

        return validateSecondDigit(digits);
    }

    private static boolean validateSecondDigit(int[] digits){
        var sum = 0;
        for (var index = 0; index < SECOND_DIGIT_MAX_INDEX; index++){
            sum += ( digits[index] * (11 - index) );
        }

        var remainder = (sum * 10) % 11;
        var check = (remainder == 10) ? 0 : remainder;

        if (check != digits[SECOND_DIGIT_MAX_INDEX]){
            return false;
        }

        return true;
    }
}
