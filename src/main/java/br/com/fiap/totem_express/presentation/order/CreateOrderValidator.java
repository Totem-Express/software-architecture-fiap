package br.com.fiap.totem_express.presentation.order;

import br.com.fiap.totem_express.application.product.ProductGateway;
import br.com.fiap.totem_express.application.user.UserGateway;
import br.com.fiap.totem_express.domain.product.Product;
import br.com.fiap.totem_express.presentation.order.requests.CreateOrderRequest;
import br.com.fiap.totem_express.presentation.order.requests.OrderItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;

//TODO: teste
@Component
public class CreateOrderValidator implements Validator {

    private final UserGateway userGateway;
    private final ProductGateway productGateway;

    public CreateOrderValidator(UserGateway userGateway, ProductGateway productGateway) {
        this.userGateway = userGateway;
        this.productGateway = productGateway;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateOrderRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        final var current = (CreateOrderRequest) target;

        final var order = current
                .orderItemsRequest()
                .stream()
                .map(OrderItemRequest::productId)
                .collect(Collectors.toSet());

        final var products = productGateway
                .findAllByIds(order)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        order.removeAll(products);

        if (!order.isEmpty()){
            final var missing = order
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            errors.reject("invalid.order.products", "The following products %s was not found.".formatted(missing));

            return;
        }


        final var identifier = current.possibleUserId();


        if (identifier.isPresent() && !userGateway.existsById(identifier.get())){
            errors.reject("invalid.order.user", "User with id %s not found".formatted(identifier.get()));
        }
    }
}
