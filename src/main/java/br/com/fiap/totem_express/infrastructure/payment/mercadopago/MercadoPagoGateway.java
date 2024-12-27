package br.com.fiap.totem_express.infrastructure.payment.mercadopago;

import br.com.fiap.totem_express.application.payment.PaymentProcessorGateway;
import br.com.fiap.totem_express.application.payment.input.GenerateQRCodeInput;
import br.com.fiap.totem_express.domain.order.OrderItem;
import br.com.fiap.totem_express.domain.product.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.POST;

//TODO: teste
public class MercadoPagoGateway implements PaymentProcessorGateway {

    private final RestTemplate restTemplate;

    private String API_TOKEN;

    private String API_URL;

    public MercadoPagoGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentProcessorResponse createPaymentQRCode(GenerateQRCodeInput input) {
        RequestEntity<PaymentQRCodeRequest> request = buildRequest(input);

        try {
            final var response = this.restTemplate.exchange(request, PaymentProcessorResponse.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RestClientException("generating QRCode from provider error" + response.getBody());
            }

            return response.getBody();

        } catch (Exception e) {
            throw new RestClientException("generating QRCode from provider error");
        }

    }

    private RequestEntity<PaymentQRCodeRequest> buildRequest(GenerateQRCodeInput input) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + this.API_TOKEN);

        return new RequestEntity<>(
                new PaymentQRCodeRequest(
                        input.transactionId(),
                        input.nameOrder(),
                        input.description(),
                        input.totalAmount(),
                        input.items().stream()
                                .map(item -> new PaymentQRCodeItem(
                                        item.title(),
                                        item.description(),
                                        item.unitPrice(),
                                        item.quantity(),
                                        item.unitMeasure(),
                                        item.totalAmount()
                                ))
                                .collect(Collectors.toList())
                ), headers, POST, URI.create(API_URL));
    }
}
