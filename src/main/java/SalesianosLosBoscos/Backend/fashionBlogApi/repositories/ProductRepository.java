package SalesianosLosBoscos.Backend.fashionBlogApi.repositories;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductName(String productName);
    // En ProductRepository
    @EntityGraph(attributePaths = {"images", "brand"})
    List<Product> findByBrandBrandId(Integer brandId);

}
