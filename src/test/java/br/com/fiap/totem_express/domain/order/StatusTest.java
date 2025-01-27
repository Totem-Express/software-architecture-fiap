package br.com.fiap.totem_express.domain.order;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


class StatusTest {

    @ParameterizedTest
    @CsvSource({
            "RECEIVED, PREPARING",
            "PREPARING, READY_TO_BE_PICKED_UP",
            "READY_TO_BE_PICKED_UP, FINISHED",
            "FINISHED, FINISHED"
    })
    void next(String currentStatus, String expectedNextStatus) {
        Status status = Status.valueOf(currentStatus);
        Status expected = Status.valueOf(expectedNextStatus);

        assertThat(status.next()).isEqualTo(expected);
    }
}