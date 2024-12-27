package br.com.fiap.totem_express.presentation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IndexControllerTest {

    @Test
    void should_redirect_to_documentation() {
        IndexController indexController = new IndexController();
        String result = indexController.redirectToSwagger();
        assertThat(result).isEqualTo("redirect:/swagger.html");
    }
}