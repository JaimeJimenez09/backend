package SalesianosLosBoscos.Backend.fashionBlogApi.repositories;

import SalesianosLosBoscos.Backend.fashionBlogApi.dto.Valoration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValorationRepository extends JpaRepository<Valoration, Integer> {
    List<Valoration> findByProductProductId(Integer productId);
    List<Valoration> findByUserUserId(Integer userId);
}
