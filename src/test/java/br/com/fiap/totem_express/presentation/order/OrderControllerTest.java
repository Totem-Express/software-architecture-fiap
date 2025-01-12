package br.com.fiap.totem_express.presentation.order;

import br.com.fiap.totem_express.TestcontainersConfiguration;
import br.com.fiap.totem_express.application.order.*;
import br.com.fiap.totem_express.application.order.output.OrderView;
import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.impl.CreateProductUseCaseImpl;
import br.com.fiap.totem_express.application.product.input.NewProductInput;
import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import br.com.fiap.totem_express.presentation.order.requests.CreateOrderRequest;
import br.com.fiap.totem_express.presentation.order.requests.OrderItemRequest;
import br.com.fiap.totem_express.presentation.product.request.CreateProductRequest;
import br.com.fiap.totem_express.shared.invariant.InvariantException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static br.com.fiap.totem_express.domain.order.Status.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UpdateOrderStatusUseCase updateStatusUseCase;

    @MockBean
    private ListOrderUseCase listUseCase;

    @MockBean
    private CreateOrderUseCase createUseCase;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    CreateProductUseCase createProductUseCase;

    @Test
    void should_return_http_201_and_created_order_when_order_is_created() throws Exception {
        ProductView productView = createProductUseCase.create(new CreateProductRequest("sandwich", "delicious", "img.png", BigDecimal.TEN, Category.DISH));
        Set<OrderItemRequest> itemsRequest = new HashSet<>();
        itemsRequest.add(new OrderItemRequest(productView.id(), 2L));
        var createOrder = new CreateOrderRequest(itemsRequest, Optional.empty());

        var orderView = new OrderView(
                LocalDateTime.now(), LocalDateTime.now(),
                new HashSet<>(), BigDecimal.valueOf(20),
                RECEIVED,
                1L,
                null
        );

        when(createUseCase.execute(createOrder)).thenReturn(orderView);

        mockMvc.perform(post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }

    @Test
    void should_return_http_400_when_order_creation_request_is_invalid() throws Exception {
        var invalidRequest = new CreateOrderRequest(new HashSet<>(), Optional.empty());

        mockMvc.perform(post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createUseCase, never()).execute(any());
    }

    @Test
    void should_return_http_200_when_order_status_is_updated() throws Exception {
        Long orderId = 1L;
        var orderView = new OrderView(
                LocalDateTime.now(), LocalDateTime.now(), new HashSet<>(),
                BigDecimal.valueOf(30),
                PREPARING,
                1L,
                null
        );

        when(updateStatusUseCase.changeStatus(orderId)).thenReturn(orderView);

        mockMvc.perform(get("/api/order/{id}/next", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("PREPARING"));
    }

    @Test
    void should_return_http_404_when_order_not_found_for_status_update() throws Exception {
        Long orderId = 999L;

        when(updateStatusUseCase.changeStatus(orderId)).thenThrow(new InvariantException("Order<%s> must exists to update status".formatted(orderId)));

        mockMvc.perform(get("/api/order/{id}/next", orderId))
                .andExpect(status().isInternalServerError());

        verify(updateStatusUseCase).changeStatus(orderId);
    }

    @Test
    void should_return_http_200_and_list_of_orders_when_orders_exist() throws Exception {
        Set<OrderItemRequest> itemsRequest = new HashSet<>();
        itemsRequest.add(new OrderItemRequest(1L, 2L));
        var createOrder1 = new CreateOrderRequest(itemsRequest, Optional.empty());
        var createOrder2 = new CreateOrderRequest(itemsRequest, Optional.empty());

        var orderView1 = new OrderView(
                LocalDateTime.now(), LocalDateTime.now(),
                new HashSet<>(), BigDecimal.valueOf(50),
                RECEIVED,
                1L,
                null
        );

        var orderView2 = new OrderView(
                LocalDateTime.now(), LocalDateTime.now(),
                new HashSet<>(), BigDecimal.valueOf(30),
                FINISHED,
                2L,
                null
        );

        when(createUseCase.execute(createOrder1)).thenReturn(orderView1);
        when(createUseCase.execute(createOrder2)).thenReturn(orderView2);


        List<OrderView> orders = Arrays.asList(orderView1, orderView2);

        when(listUseCase.execute()).thenReturn(orders);

        mockMvc.perform(get("/api/order/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("RECEIVED"))
                .andExpect(jsonPath("$[0].total").value(50))
                .andExpect(jsonPath("$[1].status").value("FINISHED"))
                .andExpect(jsonPath("$[1].total").value(30));
    }

    @Test
    void should_return_http_204_when_no_orders_exist() throws Exception {
        when(listUseCase.execute()).thenReturn(List.of());

        mockMvc.perform(get("/api/order/list"))
                .andExpect(status().isNoContent());

        verify(listUseCase).execute();
    }

}