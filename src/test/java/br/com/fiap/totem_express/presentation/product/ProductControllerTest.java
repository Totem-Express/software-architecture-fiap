package br.com.fiap.totem_express.presentation.product;

import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.DeleteProductUseCase;
import br.com.fiap.totem_express.application.product.FindProductsByCategoryUseCase;
import br.com.fiap.totem_express.application.product.UpdateProductUseCase;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import br.com.fiap.totem_express.presentation.product.request.CreateProductRequest;
import br.com.fiap.totem_express.presentation.product.request.UpdateProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.fiap.totem_express.domain.product.Category.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private JacksonTester<CreateProductRequest> createRequest;

    @Autowired
    private JacksonTester<UpdateProductRequest> updateRequest;

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

    @BeforeEach
    void setUp() {
        jwtService = mock(JWTService.class);
    }

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
                        .content(createRequest
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

    @Test
    void should_return_http_404_when_product_to_delete_not_found() throws Exception {
        Long productId = 1L;

        doThrow(new IllegalArgumentException("Product must exists invalid id " + productId)).when(deleteUseCase).delete(productId);

        mockMvc.perform(delete("/api/product/{id}", productId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_http_200_when_update_is_successful() throws Exception {
        Long productId = 29L;

        var updateProduct = new UpdateProductRequest(
                productId, "Coca-Cola 500ml",
                "Refrescante e geladinha. Uma bebida assim refresca a vida.",
                "https://cache-backend-mcd.mcdonaldscupones.com/media/image/product$kNXBvqQj/200/200/original?country=br",
                new BigDecimal("12.90"), DRINK);

        var expectedView = new ProductView.SimpleView(
                productId, "Coca-Cola 500ml",
                "Refrescante e geladinha. Uma bebida assim refresca a vida.",
                "https://cache-backend-mcd.mcdonaldscupones.com/media/image/product$kNXBvqQj/200/200/original?country=br",
                new BigDecimal("12.90"), DRINK);

        when(updateUseCase.update(any())).thenReturn(Optional.of(expectedView));

        var response = mockMvc
                .perform(put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest
                                .write(updateProduct)
                                .getJson()
                        )
                )
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(OK.value());
        assertThat(response.getContentAsString()).isEqualTo(view.write(expectedView).getJson());
    }

    @Test
    void should_return_http_404_when_product_not_found() throws Exception {
        var updateProduct = new UpdateProductRequest(
                999L,
                "Del Valle 700ml",
                "Néctar de fruta nos sabores uva ou laranja.",
                "https://cache-backend-mcd.mcdonaldscupones.com/media/image/product$kNXWVFLM/200/200/original?country=br",
                new BigDecimal("11.90"), DRINK);

        when(updateUseCase.update(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest
                                .write(updateProduct)
                                .getJson()
                        )
                )
                .andExpect(status().isNotFound());

        verify(updateUseCase).update(updateProduct);
    }

    @Test
    void should_return_http_200_with_products_when_category_is_valid_and_products_exist() throws Exception {
        String categoryName = "DISH";

        var cheddarMcMeltProduct = new ProductView.SimpleView(1L, "Cheddar McMelt",
                "Um hamburguer (100% carne bovina), molho lacteo com queijo tipo cheddar, cebola ao molho shoyu e pao escuro com gergelim.",
                "https://example.com/product1.jpg",
                new BigDecimal("19.90"), DISH);

        var bigMacProduct = new ProductView.SimpleView(2L, "Big Mac",
                "Dois hambúrgueres, alface, queijo, molho especial, cebola, picles e pão com gergelim.",
                "https://example.com/product2.jpg",
                new BigDecimal("21.90"), DISH);

        when(findAllByCategoryUseCase.findAllByCategory(DISH)).thenReturn(List.of(cheddarMcMeltProduct, bigMacProduct));

        mockMvc.perform(get("/api/product/{categoryName}", categoryName))
                .andExpect(status().isOk());

        verify(findAllByCategoryUseCase).findAllByCategory(DISH);
    }

    @Test
    void should_return_http_204_when_category_is_valid_but_no_products_exist() throws Exception {
        String categoryName = "DESSERT";

        when(findAllByCategoryUseCase.findAllByCategory(DESSERT)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/product/{categoryName}", categoryName))
                .andExpect(status().isNoContent());

        verify(findAllByCategoryUseCase).findAllByCategory(DESSERT);
    }

    @Test
    void should_return_http_404_when_category_is_invalid() throws Exception {
        String invalidCategoryName = "INVALID_CATEGORY";

        mockMvc.perform(get("/api/product/{categoryName}", invalidCategoryName))
                .andExpect(status().isNotFound());
    }

}
