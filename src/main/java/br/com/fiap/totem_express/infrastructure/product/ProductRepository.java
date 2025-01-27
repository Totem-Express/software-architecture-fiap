package br.com.fiap.totem_express.infrastructure.product;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.totem_express.domain.product.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//TODO: teste
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("SELECT p FROM product p WHERE p.category = :category AND p.deleted = false")
    List<ProductEntity> findAllByCategory(@Param("category") Category category);

    @Query("SELECT p FROM product p WHERE p.deleted = false")
    List<ProductEntity> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE product p SET p.deleted = true WHERE p.id = :id")
    void deleteById(@Param("id") Long id);
}
