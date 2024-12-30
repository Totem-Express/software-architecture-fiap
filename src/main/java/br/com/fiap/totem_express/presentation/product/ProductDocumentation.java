package br.com.fiap.totem_express.presentation.product;

import java.util.List;

import br.com.fiap.totem_express.application.product.output.ProductView;
import br.com.fiap.totem_express.presentation.errors.BadRequestError;
import br.com.fiap.totem_express.presentation.errors.NotFoundError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import br.com.fiap.totem_express.presentation.product.request.CreateProductRequest;
import br.com.fiap.totem_express.presentation.product.request.UpdateProductRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Produto", description = "API de produtos")
public interface ProductDocumentation {

    @Operation(summary = "Cria novo produto", description = "Cria um novo produto e retorna as informações fornecidas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = ProductView.SimpleView.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos estão incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
    })
    ResponseEntity<ProductView> create(@RequestBody @Valid CreateProductRequest request);

    @Operation(summary = "Exclui Produto ", description = "Exclui o produto com o ID fornecido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "O produto foi removido com sucesso" ),
            @ApiResponse(responseCode = "404", description = "ID fornecido não existe", content = {@Content(schema = @Schema(implementation = NotFoundError.class))}),
    })
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Atualiza produto", description = "Atualiza o produto com os detalhes fornecidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema=@Schema(implementation = ProductView.SimpleView.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos estão incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
    })
    ResponseEntity<ProductView> update(@RequestBody @Valid UpdateProductRequest request);

    @Operation(summary = "Traz produtos por categoria", description = "Retorna todos os produtos de acordo com a categoria fornecida")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ProductView.SimpleView.class)), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", description = "Produto sem categoria cadastrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do sistema")
    })
    ResponseEntity<List<ProductView>> findBy(@PathVariable String category);
}

