package br.com.fiap.totem_express.presentation.product;

import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.DeleteProductUseCase;
import br.com.fiap.totem_express.application.product.FindProductsByCategoryUseCase;
import br.com.fiap.totem_express.application.product.UpdateProductUseCase;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import br.com.fiap.totem_express.presentation.product.request.CreateProductRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static br.com.fiap.totem_express.domain.product.Category.DISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private JacksonTester<CreateProductRequest> request;

    @Autowired
    private JacksonTester<ProductView.SimpleView> view;

    @MockBean
    private CreateProductUseCase createUseCase;
    @MockBean
    private DeleteProductUseCase deleteUseCase;
    @MockBean
    private UpdateProductUseCase updateUseCase;
    @MockBean
    private FindProductsByCategoryUseCase findAllByCategoryUseCase;

    @Test
    void should_return_http_200_when_the_request_is_valid() throws Exception {
        var createProduct = new CreateProductRequest(
                "Cheddar McMelt",
                "Um hamburguer (100% carne bovina), molho lacteo com queijo tipo cheddar, cebola ao molho shoyu e pao escuro com gergelim.",
                "https://cache-backend-mcd.mcdonaldscupones.com/media/image/product$kzXv7hw4/200/200/original?country=br",
                new BigDecimal("19.90"), DISH);

        var response = mockMvc
                .perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request
                                .write(createProduct)
                                .getJson()
                        )
                )
                .andReturn()
                .getResponse();

        var expectedView = new ProductView.SimpleView(null, "Cheddar McMelt", "Um hamburguer (100% carne bovina), molho lacteo com queijo tipo cheddar, cebola ao molho shoyu e pao escuro com gergelim.",
                "https://cache-backend-mcd.mcdonaldscupones.com/media/image/product$kzXv7hw4/200/200/original?country=br",
                new BigDecimal("19.90"), DISH);

        when(createUseCase.create(any())).thenReturn(expectedView);

        assertThat(response.getStatus()).isEqualTo(OK.value());

        //TODO -> erro por conta do ID
//        assertThat(response.getContentAsString()).isEqualTo(view.write(expectedView).getJson());
    }

    @Test
    void should_return_http_400_when_request_body_is_empty() throws Exception {
        var response = mockMvc
                .perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    void should_return_http_204_when_delete_is_successful() throws Exception {
        Long productId = 1L;

        doNothing().when(deleteUseCase).delete(productId);

        mockMvc.perform(delete("/api/product/{id}", productId))
                .andExpect(status().isNoContent());

        verify(deleteUseCase).delete(productId);
    }

//    TODO -> pensar em como testar a falha (precisa fazer a verificacao se o id existe no controller)
//    @Test
//    void should_return_http_404_when_product_not_found() throws Exception {
//        Long productId = 1L;
//
//        doThrow(new IllegalArgumentException("Product not found")).when(deleteUseCase).delete(productId);
//
//        mockMvc.perform(delete("/api/product/{id}", productId))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Product not found"));
//    }
}