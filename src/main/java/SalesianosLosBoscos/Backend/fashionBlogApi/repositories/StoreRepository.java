package SalesianosLosBoscos.Backend.fashionBlogApi.repositories;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    @Query("SELECT s FROM Store s JOIN s.products p WHERE p.productId = :productId")
    List<Store> findStoresByProductId(Integer productId);
}
