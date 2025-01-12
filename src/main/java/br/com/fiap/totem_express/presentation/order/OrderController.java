package br.com.fiap.totem_express.presentation.order;

import br.com.fiap.totem_express.application.order.CreateOrderUseCase;
import br.com.fiap.totem_express.application.order.ListOrderUseCase;
import br.com.fiap.totem_express.application.order.UpdateOrderStatusUseCase;
import br.com.fiap.totem_express.application.order.output.OrderView;
import br.com.fiap.totem_express.infrastructure.security.ApplicationUser;
import br.com.fiap.totem_express.presentation.order.requests.CreateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
public class OrderController implements OrderDocumentation {

    private final CreateOrderValidator orderValidator;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final ListOrderUseCase listOrderUseCase;
    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderValidator orderValidator, UpdateOrderStatusUseCase updateOrderStatusUseCase, ListOrderUseCase listOrderUseCase, CreateOrderUseCase createOrderUseCase) {
        this.orderValidator = orderValidator;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.listOrderUseCase = listOrderUseCase;
        this.createOrderUseCase = createOrderUseCase;
    }

    @InitBinder("createOrderRequest")
    public void init(WebDataBinder it){
        it.addValidators(orderValidator);
    }

    @Override
    @GetMapping("/api/order/list")
    public ResponseEntity<List<OrderView>> list() {
        List<OrderView> orders = listOrderUseCase.execute();
        if (orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/api/order/{id}/next")
    public ResponseEntity<?> goToNextStatus(@PathVariable("id") Long id){
        return ResponseEntity.ok(updateOrderStatusUseCase.changeStatus(id));
    }

    @PostMapping("/api/order/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        if (Objects.requireNonNull(createOrderRequest.orderItems()).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final var request = ApplicationUser
                .retrieveUserId()
                .map(createOrderRequest::with)
                .orElse(createOrderRequest);

        OrderView execute = createOrderUseCase.execute(request);
        return ResponseEntity.status(201).body(execute);
    }
}
