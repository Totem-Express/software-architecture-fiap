package br.com.fiap.totem_express.presentation.product;

import java.util.List;
import java.util.Optional;

import br.com.fiap.totem_express.application.product.output.ProductView;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.totem_express.application.product.CreateProductUseCase;
import br.com.fiap.totem_express.application.product.DeleteProductUseCase;
import br.com.fiap.totem_express.application.product.FindProductsByCategoryUseCase;
import br.com.fiap.totem_express.application.product.UpdateProductUseCase;
import br.com.fiap.totem_express.domain.product.Category;
import br.com.fiap.totem_express.presentation.product.request.CreateProductRequest;
import br.com.fiap.totem_express.presentation.product.request.UpdateProductRequest;
import jakarta.transaction.Transactional;

@RestController
public class ProductController implements ProductDocumentation {

    private final CreateProductUseCase createUseCase;
    private final DeleteProductUseCase deleteUseCase;
    private final UpdateProductUseCase updateUseCase;
    private final FindProductsByCategoryUseCase findAllByCategoryUseCase;

    public ProductController(CreateProductUseCase createUseCase, DeleteProductUseCase deleteUseCase,
                             UpdateProductUseCase updateUseCase, FindProductsByCategoryUseCase findAllByCategoryUseCase) {
        this.createUseCase = createUseCase;
        this.deleteUseCase = deleteUseCase;
        this.updateUseCase = updateUseCase;
        this.findAllByCategoryUseCase = findAllByCategoryUseCase;
    }

    @Override
    @Transactional
    @PostMapping("/api/product")
    public ResponseEntity<ProductView> create(@RequestBody @Valid CreateProductRequest request) {
        final var view = createUseCase.create(request);
        return ResponseEntity.ok(view);
    }

    @Override
    @Transactional
    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    @PutMapping("/api/product")
    public ResponseEntity<ProductView> update(@RequestBody @Valid UpdateProductRequest request) {
        Optional<ProductView> possibleUpdateProduct = updateUseCase.update(request);
        if (possibleUpdateProduct.isEmpty()) return ResponseEntity.notFound().build();

        final var view = possibleUpdateProduct.get();
        return ResponseEntity.ok(view);
    }

    @Override
    @Transactional
    @GetMapping("/api/product/{categoryName}")
    public ResponseEntity<List<ProductView>> findBy(@PathVariable String categoryName) {
        Optional<Category> possibleCategory = Category.findByName(categoryName);
        if (possibleCategory.isEmpty()) return ResponseEntity.notFound().build();

        Category category = possibleCategory.get();
        List<ProductView> view = findAllByCategoryUseCase.findAllByCategory(category);

        if (view.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(view);
    }
}
